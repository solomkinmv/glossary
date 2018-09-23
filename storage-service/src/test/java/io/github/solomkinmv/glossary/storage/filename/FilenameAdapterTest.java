package io.github.solomkinmv.glossary.storage.filename;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FilenameAdapterTest {

    private FilenameAdapter filenameAdapter;

    @Before
    public void setUp() {
        filenameAdapter = new FilenameAdapter();
    }

    @Test
    public void removesMultipleSpaces() {
        String originalString = "a b  c.png";

        String actualString = filenameAdapter.adapt(originalString);

        assertThat(actualString).isEqualTo("a-b-c.png");
    }

    @Test
    public void removesMultipleSlashes() {
        String originalString = "a 213 \\ // -> ± bc.png";

        String actualString = filenameAdapter.adapt(originalString);

        assertThat(actualString).doesNotContain(" ");
        assertThat(actualString).doesNotContain("/");
        assertThat(actualString).doesNotContain("\\");
    }

    @Test
    public void doesNotBreakFileExtension() {
        String fileNameWithExtension = "file one.png";

        String actualFileName = filenameAdapter.adapt(fileNameWithExtension);

        assertThat(actualFileName).isEqualTo("file-one.png");
    }

    @Test
    public void handlesComas() {
        String fileNameWithExtension = "hello, dolly.mp3";

        String actualFileName = filenameAdapter.adapt(fileNameWithExtension);

        assertThat(actualFileName).isEqualTo("hello-dolly.mp3");
    }

    @Test
    public void spacesMatters() {
        String adapted1 = filenameAdapter.adapt("ab c.png");
        String adapted2 = filenameAdapter.adapt("a bc.png");

        assertThat(adapted1).isNotEqualTo(adapted2);
    }
}