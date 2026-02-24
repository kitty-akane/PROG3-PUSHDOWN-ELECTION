package pushdown2.model;

public class ElectionResult {

    private String candidateName;
    private long validVoteCount;

    public ElectionResult(String candidateName, long validVoteCount) {
        this.candidateName = candidateName;
        this.validVoteCount = validVoteCount;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public long getValidVoteCount() {
        return validVoteCount;
    }

    @Override
    public String toString() {
        return "ElectionResult(candidateName=" + candidateName + ", validVoteCount=" + validVoteCount + ")";
    }
}
