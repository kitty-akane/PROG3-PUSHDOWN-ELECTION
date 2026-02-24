package pushdown2;

import pushdown2.dao.DBConnection;
import pushdown2.dao.DataRetriever;
import pushdown2.model.CandidateVoteCount;
import pushdown2.model.VoteTypeCount;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
       DataRetriever dr = new DataRetriever();

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║     ELECTION PUSH-DOWN TESTS         ║");
        System.out.println("╚══════════════════════════════════════╝\n");

        testConnection();
        testQ1(dr);
        testQ2(dr);
        testQ3(dr);
        testQ4(dr);
        testQ5(dr);
        testQ6(dr);

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║            This is the end           ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    static void testConnection() {
        System.out.println("┌─ Test Connection) DBConnection.getConnection()");
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("└─ PASS: Connexion réussie à " + conn.getMetaData().getURL());
            }
        } catch (Exception e) {
            System.out.println("└─ FAIL: " + e.getMessage());
        }
        System.out.println();
    }

    static void testQ1(DataRetriever dr) {
        System.out.println("┌─ Test Q1) countAllVotes()");
        System.out.println("│  Nombre total de votes");
        System.out.println("│  ─────────────────────");
        System.out.println("│  total_votes");
        System.out.println("│  " + dr.countAllVotes());
        System.out.println("└──────────────────────\n");
    }

    static void testQ2(DataRetriever dr) {
        System.out.println("┌─ Test Q2) countVotesByType()");
        System.out.println("│  Votes par type");
        System.out.println("│  ──────────────────────────");
        System.out.println("│  vote_type  | count");
        System.out.println("│  ──────────────────────────");
        for (VoteTypeCount v : dr.countVotesByType()) {
            System.out.printf("│  %-10s | %d%n", v.getVoteType(), v.getCount());
        }
        System.out.println("└──────────────────────\n");
    }

    static void testQ3(DataRetriever dr) {
        System.out.println("┌─ Test Q3) countValidVotesByCandidate()");
        System.out.println("│  Votes valides par candidat");
        System.out.println("│  ──────────────────────────────────");
        System.out.println("│  candidate_name | valid_votes");
        System.out.println("│  ──────────────────────────────────");
        for (CandidateVoteCount c : dr.countValidVotesByCandidate()) {
            System.out.printf("│  %-16s | %d%n", c.getCandidateName(), c.getValidVoteCount());
        }
        System.out.println("└──────────────────────\n");
    }

    static void testQ4(DataRetriever dr) {
        System.out.println("┌─ Test Q4) computeVoteSummary()");
        System.out.println("│  Synthèse globale des votes");
        System.out.println("│  ──────────────────────────────────────────");
        System.out.println("│  valid_count | blank_count | null_count");
        System.out.println("│  ──────────────────────────────────────────");
        var s = dr.computeVoteSummary();
        System.out.printf("│  %-12d | %-11d | %d%n",
                s.getValidCount(), s.getBlankCount(), s.getNullCount());
        System.out.println("└──────────────────────\n");
    }

    static void testQ5(DataRetriever dr) {
        System.out.println("┌─ Test Q5) computeTurnoutRate()");
        System.out.println("│  Taux de participation");
        System.out.println("│  ─────────────────────────────");
        System.out.printf( "│  Taux = %.0f%%%n", dr.computeTurnoutRate());
        System.out.println("└──────────────────────\n");
    }

    static void testQ6(DataRetriever dr) {
        System.out.println("┌─ Test Q6) findWinner()");
        System.out.println("│  Résultat de l'élection");
        System.out.println("│  ──────────────────────────────────");
        System.out.println("│  candidate_name | valid_vote_count");
        System.out.println("│  ──────────────────────────────────");
        var w = dr.findWinner();
        System.out.printf( "│  %-16s | %d%n", w.getCandidateName(), w.getValidVoteCount());
        System.out.println("└──────────────────────\n");
    }
}
