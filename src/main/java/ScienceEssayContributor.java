public interface ScienceEssayContributor {
    void contributeTo(ScienceEssayPublisher publisher);

    long submit(Essay essay, ScienceEssayPublisher publisher);

    String getName();
}
