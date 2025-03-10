package org.kohsuke.github.graphql.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A response of GraphQL.
 * <p>
 * This class is used to parse the response of GraphQL.
 * </p>
 *
 * @param <T> the type of data
 */
public class GHGraphQLResponse<T> {

    private final T data;

    private final List<GHGraphQLError> errors;

    @JsonCreator
    public GHGraphQLResponse(@JsonProperty("data") T data, @JsonProperty("errors") List<GHGraphQLError> errors) {
        this.data = data;
        this.errors = errors;
    }

    public Boolean isSuccessful() {
        return errors == null || errors.isEmpty();
    }

    public T getData() {
        if (!isSuccessful()) {
            throw new RuntimeException("This response is Errors occurred response");
        }

        return data;
    }

    public List<String> getErrorMessages() {
        if (isSuccessful()) {
            throw new RuntimeException("No errors occurred");
        }

        return errors.stream().map(GHGraphQLError::getErrorMessage).collect(Collectors.toList());
    }

    /**
     * A error of GraphQL response.
     * Minimum implementation for GraphQL error.
     */
    private static class GHGraphQLError {

        private final String errorMessage;

        @JsonCreator
        public GHGraphQLError(@JsonProperty("message") String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
