package io.github.solomkinmv.glossary.web;

import io.github.solomkinmv.glossary.persistence.model.RoleType;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import io.github.solomkinmv.glossary.web.security.model.JsonWebToken;
import io.github.solomkinmv.glossary.web.security.util.JwtTokenFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Slf4j
public abstract class MockMvcBase {
    protected final MediaType contentType = new MediaType(MediaTypes.HAL_JSON,
                                                          StandardCharsets.UTF_8);
    @Rule
    public JUnitRestDocumentation restDocumentation =
            new JUnitRestDocumentation("build/generated-snippets");
    protected MockMvc mockMvc;
    protected RequestHeadersSnippet headersSnippet;
    protected RestDocumentationResultHandler documentationHandler;
    @Autowired
    protected JsonConverter jsonConverter;
    private AuthenticatedUser authenticatedUser = new AuthenticatedUser(
            "user1",
            Collections.singletonList(new SimpleGrantedAuthority(RoleType.USER.authority())));
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private JwtTokenFactory tokenFactory;

    protected RequestPostProcessor userToken() {
        return request -> {
            // If the tests requires setup logic for users, you can place it here.
            // Authorization headers or cookies for users should be added here as well.
            String accessToken = getAccessToken();
            request.addHeader("X-Authorization", "Bearer " + accessToken);
            return request;
        };
    }

    private String getAccessToken() {
        JsonWebToken accessToken = tokenFactory.createAccessJwtToken(getAuthenticatedUser());
        return accessToken.getRawToken();
    }

    protected AuthenticatedUser getAuthenticatedUser() {
        return authenticatedUser;
    }

    @Before
    public void setUpMockMvc() throws Exception {
        log.info("Setting base mock MVC test");


        documentationHandler = document("{class-name}/{method-name}",
                                        preprocessRequest(prettyPrint()),
                                        preprocessResponse(prettyPrint()));
        mockMvc = webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(documentationHandler)
                .build();

        headersSnippet = requestHeaders(headerWithName("X-Authorization").description(
                "JWT authentication token in following format: 'Bearer <token>'"));
    }
}
