import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("An science portal")
class SciencePortalTest {
    @Test
    @DisplayName("can be queried for personally submitted essays")
    void testQueryAuthorsSubmissions() {
        SciencePortal acm = new SciencePortal("ACM");
        Author author = new Author("Author 1");
        Author otherAuthor = new Author("Author 2");

        author.contributeTo(acm);
        author.reviewFor(acm);

        otherAuthor.contributeTo(acm);
        otherAuthor.reviewFor(acm);

        Essay essay1 = EssayBuilder.newEssay().withTitle("Simple Essay").complete();
        Essay essay2 = EssayBuilder.newEssay().withTitle("Simple Essay2").complete();
        Essay essay3 = EssayBuilder.newEssay().withTitle("Simple Essay3").complete();
        author.submit(essay1, acm);
        otherAuthor.submit(essay2, acm);
        otherAuthor.submit(essay3, acm);

        Submissions expected1 = new Submissions(
                List.of(
                        new Submission(author, essay1)
                )
        );

        Submissions expected2 = new Submissions(
                List.of(
                        new Submission(otherAuthor, essay2),
                        new Submission(otherAuthor, essay3)
                )
        );

        assertEquals(expected1, acm.getSubmissionsOf(author));
        assertNotEquals(expected2, acm.getSubmissionsOf(author));
        assertEquals(expected2, acm.getSubmissionsOf(otherAuthor));

        assertThrows(IllegalArgumentException.class, () -> acm.contribute(new Submission(null, null)));

    }

    @Test
    @DisplayName("can be queried for number of submissions containing certain strings in their title")
    void testTitles() {
        SciencePortal acm = new SciencePortal("ACM");
        Author author = new Author("Author 1");
        Author otherAuthor = new Author("Author 2");

        author.contributeTo(acm);
        author.reviewFor(acm);
        otherAuthor.contributeTo(acm);
        otherAuthor.reviewFor(acm);
        String publisherName = acm.getName();

        author.submit(EssayBuilder.newEssay().withTitle("First Essay").complete(), acm);
        otherAuthor.submit(EssayBuilder.newEssay().withTitle("Second Third Essay").complete(), acm);
        otherAuthor.submit(EssayBuilder.newEssay().withTitle("Second Fourth Essay").complete(), acm);

        assertEquals(1, acm.countSubmissionsWithTitleContaining("First"));
        assertEquals(2, acm.countSubmissionsWithTitleContaining("Second"));
        assertEquals(1, acm.countSubmissionsWithTitleContaining("Third"));
        assertEquals(1, acm.countSubmissionsWithTitleContaining("Fourth"));
        assertEquals(3, acm.countSubmissionsWithTitleContaining("Essay"));
    }

    @Test
    @DisplayName("can be queried for all titles of all submissions")
    void queryTitlesOfAllSubmissions() {
        String titleA = "A";
        String titleB = "B";
        String titleC = "C";
        Submissions submissions = new Submissions(List.of(
                new Submission(
                        new Author("AuthorA"),
                        EssayBuilder.newEssay().withTitle(titleA).complete()
                ),
                new Submission(
                        new Author("AuthorB"),
                        EssayBuilder.newEssay().withTitle(titleA).complete()
                ),
                new Submission(
                        new Author("AuthorC"),
                        EssayBuilder.newEssay().withTitle(titleB).complete()
                ),
                new Submission(
                        new Author("AuthorD"),
                        EssayBuilder.newEssay().withTitle(titleB).complete()
                ),
                new Submission(
                        new Author("AuthorE"),
                        EssayBuilder.newEssay().withTitle(titleC).complete()
                )
        ));
        SciencePortal acm = new SciencePortal("ACM", submissions);

        assertEquals(Set.of(titleA, titleB, titleC), acm.getTitlesOfAllSubmissions());
    }

    @Test
    @DisplayName("can be asked if a reviewer is also a contributor")
    void queryReviewerIsContributor() {
        SciencePortal acm = new SciencePortal("ACM");

        Author author = new Author("x");
        assertFalse(acm.isContributor(author));

        author.contributeTo(acm);
        assertFalse(acm.isContributor(author));

        author.reviewFor(acm);
        assertTrue(acm.isContributor(author));

    }
}
