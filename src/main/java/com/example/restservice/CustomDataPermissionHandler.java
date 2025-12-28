package com.example.restservice;

import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;

// You need to implement the logic to get the current user's permission SQL segment
public class CustomDataPermissionHandler implements DataPermissionHandler {

    @Override
    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        // In a real application, you would fetch the user's permission rules here
        // based on the current context (e.g., from a security context).

        // Example: Restrict data to a specific department IDs (e.g., '1', '2', '3')
        String permissionSql = "department_id IN ('1', '2')";

        try {
            // Parse the custom SQL fragment into an Expression
            Expression permissionExpression = CCJSqlParserUtil.parseCondExpression(permissionSql);

            // If an existing WHERE clause exists, combine them using AND
            if (where != null) {
                return new AndExpression(where, permissionExpression);
            }

            // Otherwise, just return the permission expression
            return permissionExpression;

        } catch (Exception e) {
            e.printStackTrace();
            return where; // Return original where clause on error or if no permission is needed
        }
    }
}
