package pushdown2;

import pushdown2.dao.DBConnection;
import pushdown2.dao.DataRetriever;
import pushdown2.model.CandidateVoteCount;
import pushdown2.model.VoteTypeCount;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
       DataRetriever dr = new DataRetriever();

        System.out.println("=== ELECTION PUSH-DOWN TESTS ===\n");

        testConnection();
        testQ1(dr);
        testQ2(dr);
        testQ3(dr);
        testQ4(dr);
        testQ5(dr);
        testQ6(dr);

        System.out.println("=== THE END FOLKS! ===\n");
    }

    static void testConnection() {
        System.out.println("==== Connection test");
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Pass granted to: " + conn.getMetaData().getURL());
            }
        } catch (Exception e) {
            System.out.println("Oops! " + e.getMessage());
        }
        System.out.println();
    }

    static void testQ1(DataRetriever dr) {
        System.out.println(" Test Q1) countAllVotes()");
        System.out.println("   total_votes : " + dr.countAllVotes());
        System.out.println();
    }

    static void testQ2(DataRetriever dr) {
        System.out.println(" Test Q2) countVotesByType()");
        for (VoteTypeCount v : dr.countVotesByType()) {
            System.out.printf("   %-10s : %d%n", v.getVoteType(), v.getCount());
        }
        System.out.println();
    }

    static void testQ3(DataRetriever dr) {
        System.out.println(" Test Q3) countValidVotesByCandidate()");
        for (CandidateVoteCount c : dr.countValidVotesByCandidate()) {
            System.out.printf("   %-12s : %d vote(s) valide(s)%n",
                    c.getCandidateName(), c.getValidVoteCount());
        }
        System.out.println();
    }

    static void testQ4(DataRetriever dr) {
        System.out.println("┌─ Test Q4) computeVoteSummary()");
        var s = dr.computeVoteSummary();
        System.out.println("   " + s);
        System.out.println();
    }

    static void testQ5(DataRetriever dr) {
        System.out.println(" Test Q5) computeTurnoutRate()");
        System.out.printf("   Taux de participation : %.0f%%%n", dr.computeTurnoutRate());
        System.out.println();
    }

    static void testQ6(DataRetriever dr) {
        System.out.println(" Test Q6) findWinner()");
        var w = dr.findWinner();
        System.out.printf("   Gagnant : %s avec %d vote(s) valide(s)%n",
                w.getCandidateName(), w.getValidVoteCount());
        System.out.println();
    }
}
