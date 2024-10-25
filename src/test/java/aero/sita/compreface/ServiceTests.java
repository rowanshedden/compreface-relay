package aero.sita.compreface;

import aero.sita.compreface.models.dto.GalleryAction;
import aero.sita.compreface.models.dto.SubjectResponse;
import aero.sita.compreface.services.FeedService;
import aero.sita.compreface.utils.MiscUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.UnorderedRequestExpectationManager;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest()
public class ServiceTests {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FeedService feedService;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.bindTo(restTemplate).build(new UnorderedRequestExpectationManager());
    }

    @Test
    public void process_add_request() {
        try {
            GalleryAction galleryAction = (GalleryAction) MiscUtil.fromJson(this.readFile("gallery-action-from-webapp-no-itn.json"), GalleryAction.class);
            assertThat(galleryAction).isNotNull();

            String subjectResponse = (String) this.readFile("compreface-response.json");
            assertThat(subjectResponse).isNotNull();

            /*
             * mock the CompreFace add subject call
             */
            mockServer.expect(ExpectedCount.between(0, 1), requestTo(new URI("http://192.168.1.181/api/v1/recognition/faces?subject=Rowan&det_prob_threshold=0.8")))
                    .andExpect(method(HttpMethod.POST))
                    .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(subjectResponse));

            // run the test
            SubjectResponse response = feedService.addSubject(galleryAction);
            log.info("SubjectResponse: {}", response);

            // check the result
            mockServer.verify();
            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getLocalizedMessage());
        }
    }

    private String readFile(String fileName) throws IOException {
        var classLoader = getClass().getClassLoader();
        return new String(Objects.requireNonNull(classLoader.getResourceAsStream(fileName)).readAllBytes());
    }

}
