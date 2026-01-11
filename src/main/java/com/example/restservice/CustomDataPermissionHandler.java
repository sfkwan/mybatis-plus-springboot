package com.example.restservice;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;

// You need to implement the logic to get the current user's permission SQL segment
@Slf4j
public class CustomDataPermissionHandler implements DataPermissionHandler {
    @Override
    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        // Get the current user from Spring Security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        List<String> userRoles = null;

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails userDetails) {
                username = userDetails.getUsername();
            } else {
                username = principal.toString();
            }

            // Extract user roles
            userRoles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
        }

        log.info("Generating data permission SQL for user: {}, roles: {}", username, userRoles);

        // Example: Restrict data to a specific department IDs based on the
        // authenticated user
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
