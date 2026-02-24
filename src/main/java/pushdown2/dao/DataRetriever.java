package pushdown2.dao;

import pushdown2.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    // Q1 - Nombre total de votes

    public long countAllVotes() {
        String sql = "SELECT COUNT(*) AS total_votes FROM vote";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong("total_votes");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return 0;
    }

    // Q2 - Nombre de votes par type
    public List<VoteTypeCount> countVotesByType() {
        String sql = """
                SELECT vote_type, COUNT(*) AS count
                FROM vote
                GROUP BY vote_type
                ORDER BY vote_type
                """;

        List<VoteTypeCount> result = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new VoteTypeCount(
                        VoteType.valueOf(rs.getString("vote_type")),
                        rs.getLong("count")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    // Q3 - Nombre de votes valides par candidat
    public List<CandidateVoteCount> countValidVotesByCandidate() {
        String sql = """
                SELECT c.name AS candidate_name,
                       COUNT(CASE WHEN v.vote_type = 'VALID' THEN 1 END) AS valid_vote
                FROM candidate c
                LEFT JOIN vote v ON v.candidate_id = c.id
                GROUP BY c.id, c.name
                ORDER BY valid_vote DESC
                """;

        List<CandidateVoteCount> result = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new CandidateVoteCount(
                        rs.getString("candidate_name"),
                        rs.getLong("valid_vote")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    // Q4 - Synthèse globale des votes
    public VoteSummary computeVoteSummary() {
        String sql = """
                SELECT
                    COUNT(CASE WHEN vote_type = 'VALID' THEN 1 END) AS valid_count,
                    COUNT(CASE WHEN vote_type = 'BLANK' THEN 1 END) AS blank_count,
                    COUNT(CASE WHEN vote_type = 'NULL'  THEN 1 END) AS null_count
                FROM vote
                """;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return new VoteSummary(
                        rs.getLong("valid_count"),
                        rs.getLong("blank_count"),
                        rs.getLong("null_count")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return new VoteSummary(0, 0, 0);
    }

    // Q5 - Taux de participation
    public double computeTurnoutRate() {
        String sql = """
                SELECT
                    COUNT(DISTINCT v.voter_id) AS voters_who_voted,
                    (SELECT COUNT(*) FROM voter) AS total_voters
                FROM vote v
                """;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                long votersWhoVoted = rs.getLong("voters_who_voted");
                long totalVoters = rs.getLong("total_voters");
                return (double) votersWhoVoted / totalVoters * 100;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return 0.0;
    }

    // Q6 - Résultat élection
    public ElectionResult findWinner() {
        String sql = """
                SELECT c.name AS candidate_name,
                       COUNT(v.id) AS valid_vote_count
                FROM candidate c
                LEFT JOIN vote v ON v.candidate_id = c.id AND v.vote_type = 'VALID'
                GROUP BY c.id, c.name
                ORDER BY valid_vote_count DESC
                LIMIT 1
                """;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return new ElectionResult(
                        rs.getString("candidate_name"),
                        rs.getLong("valid_vote_count")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
}
