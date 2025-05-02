package com.example.project.dao;

import java.sql.SQLException;

public interface ISecurityQuestionDAO {

    /**
     * Saves or updates the security question and answer for a user.
     *
     * @param email    The user's email.
     * @param question The selected security question.
     * @param answer   The answer to the security question.
     * @throws SQLException If any database error occurs.
     */
    void saveSecurityQuestion(String email, String question, String answer) throws SQLException;

    /**
     * Retrieves the security question and answer for a given user.
     *
     * @param email The user's email.
     * @return A String array with [0] = question, [1] = answer. Null if not found.
     * @throws SQLException If any database error occurs.
     */
    String[] getSecurityQuestion(String email) throws SQLException;
}
