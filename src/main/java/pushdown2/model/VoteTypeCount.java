package pushdown2.model;

public class VoteTypeCount {

    private VoteType voteType;
    private long count;

    public VoteTypeCount(VoteType voteType, long count) {
        this.voteType = voteType;
        this.count = count;
    }

    public VoteType getVoteType() {
        return voteType;
    }

    public long getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "VoteTypeCount(voteType=" + voteType + ", count=" + count + ")";
    }
}
