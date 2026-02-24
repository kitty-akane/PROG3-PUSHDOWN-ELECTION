-- Q1 : Nombre total de votes

SELECT COUNT(id) AS total_votes FROM vote;
-- Q2 : Nombre de votes par type

SELECT vote_type, COUNT(*) AS count
FROM vote
GROUP BY
    vote_type
ORDER BY vote_type;

-- Q3 : Nombre de votes valides par candidat

SELECT c.name AS candidate_name, COUNT(
        CASE
            WHEN v.vote_type = 'VALID' THEN 1
        END
    ) AS valid_vote
FROM candidate c
    LEFT JOIN vote v ON v.candidate_id = c.id
GROUP BY
    c.id,
    c.name
ORDER BY valid_vote DESC;

-- Q4 : Synthèse globale des votes

SELECT
    COUNT(
        CASE
            WHEN vote_type = 'VALID' THEN 1
        END
    ) AS valid_count,
    COUNT(
        CASE
            WHEN vote_type = 'BLANK' THEN 1
        END
    ) AS blank_count,
    COUNT(
        CASE
            WHEN vote_type = 'NULL' THEN 1
        END
    ) AS null_count
FROM vote;

-- Q5 : Taux de participation

SELECT
    COUNT(DISTINCT v.voter_id) AS voters_who_voted,
    (
        SELECT COUNT(*)
        FROM voter
    ) AS total_voters,
    ROUND(
        COUNT(DISTINCT v.voter_id) * 100.0 / (
            SELECT COUNT(*)
            FROM voter
        ),
        2
    ) AS turnout_rate
FROM vote v;

-- Q6 : Résultat élection - Candidat gagnant

SELECT
    c.name AS candidate_name,
    COUNT(v.id) AS valid_vote_count
FROM candidate c
    LEFT JOIN vote v ON v.candidate_id = c.id
    AND v.vote_type = 'VALID'
GROUP BY
    c.id,
    c.name
ORDER BY valid_vote_count DESC
LIMIT 1;