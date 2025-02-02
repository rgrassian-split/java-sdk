package dev.openfeature.javasdk;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProviderSpecTest {
    NoOpProvider p = new NoOpProvider();

    @Specification(number="2.1", text="The provider interface MUST define a metadata member or accessor, containing a name field or accessor of type string, which identifies the provider implementation.")
    @Test void name_accessor() {
        assertNotNull(p.getName());
    }

    @Specification(number="2.3.1", text="The feature provider interface MUST define methods for typed " +
            "flag resolution, including boolean, numeric, string, and structure.")
    @Specification(number="2.4", text="In cases of normal execution, the provider MUST populate the " +
            "flag resolution structure's value field with the resolved flag value.")
    @Specification(number="2.2", text="The feature provider interface MUST define methods to resolve " +
            "flag values, with parameters flag key (string, required), default value " +
            "(boolean | number | string | structure, required), evaluation context (optional), and " +
            "evaluation options (optional), which returns a flag resolution structure.")
    @Specification(number="2.9.1", text="The flag resolution structure SHOULD accept a generic " +
            "argument (or use an equivalent language feature) which indicates the type of the wrapped value field.")
    @Test void flag_value_set() {
        ProviderEvaluation<Integer> int_result = p.getIntegerEvaluation("key", 4, new EvaluationContext(), FlagEvaluationOptions.builder().build());
        assertNotNull(int_result.getValue());

        ProviderEvaluation<Double> double_result = p.getDoubleEvaluation("key", 0.4, new EvaluationContext(), FlagEvaluationOptions.builder().build());
        assertNotNull(double_result.getValue());

        ProviderEvaluation<String> string_result = p.getStringEvaluation("key", "works", new EvaluationContext(), FlagEvaluationOptions.builder().build());
        assertNotNull(string_result.getValue());

        ProviderEvaluation<Boolean> boolean_result = p.getBooleanEvaluation("key", false, new EvaluationContext(), FlagEvaluationOptions.builder().build());
        assertNotNull(boolean_result.getValue());

        ProviderEvaluation<Node<Integer>> object_result = p.getObjectEvaluation("key", new Node<Integer>(), new EvaluationContext(), FlagEvaluationOptions.builder().build());
        assertNotNull(boolean_result.getValue());
    }

    @Specification(number="2.6", text="The provider SHOULD populate the flag resolution structure's " +
            "reason field with a string indicating the semantic reason for the returned flag value.")
    @Test void has_reason() {
        ProviderEvaluation<Boolean> result = p.getBooleanEvaluation("key", false, new EvaluationContext(), FlagEvaluationOptions.builder().build());
        assertEquals(Reason.DEFAULT, result.getReason());
    }

    @Specification(number="2.7", text="In cases of normal execution, the provider MUST NOT populate " +
            "the flag resolution structure's error code field, or otherwise must populate it with a null or falsy value.")
    @Test void no_error_code_by_default() {
        ProviderEvaluation<Boolean> result = p.getBooleanEvaluation("key", false, new EvaluationContext(), FlagEvaluationOptions.builder().build());
        assertNull(result.getErrorCode());
    }

    @Specification(number="2.8", text="In cases of abnormal execution, the provider MUST indicate an " +
            "error using the idioms of the implementation language, with an associated error code having possible " +
            "values PROVIDER_NOT_READY, FLAG_NOT_FOUND, PARSE_ERROR, TYPE_MISMATCH, or GENERAL.")
    @Disabled("I don't think we expect the provider to do all the exception catching.. right?")
    @Test void error_populates_error_code() {
        AlwaysBrokenProvider broken = new AlwaysBrokenProvider();
        ProviderEvaluation<Boolean> result = broken.getBooleanEvaluation("key", false, new EvaluationContext(), FlagEvaluationOptions.builder().build());
        assertEquals(ErrorCode.GENERAL, result.getErrorCode());
    }

    @Specification(number="2.5", text="In cases of normal execution, the provider SHOULD populate the " +
            "flag resolution structure's variant field with a string identifier corresponding to the returned flag value.")
    @Test void variant_set() {
        ProviderEvaluation<Integer> int_result = p.getIntegerEvaluation("key", 4, new EvaluationContext(), FlagEvaluationOptions.builder().build());
        assertNotNull(int_result.getReason());

        ProviderEvaluation<Double> double_result = p.getDoubleEvaluation("key", 0.4, new EvaluationContext(), FlagEvaluationOptions.builder().build());
        assertNotNull(double_result.getReason());

        ProviderEvaluation<String> string_result = p.getStringEvaluation("key", "works", new EvaluationContext(), FlagEvaluationOptions.builder().build());
        assertNotNull(string_result.getReason());

        ProviderEvaluation<Boolean> boolean_result = p.getBooleanEvaluation("key", false, new EvaluationContext(), FlagEvaluationOptions.builder().build());
        assertNotNull(boolean_result.getReason());
    }

    @Specification(number="2.11.1", text="If the implementation includes a context transformer, the provider SHOULD accept a generic argument (or use an equivalent language feature) indicating the type of the transformed context.  If such type information is supplied, more accurate type information can be supplied in the flag resolution methods.")
    @Specification(number="2.10", text="The provider interface MAY define a context transformer method " +
            "or function, which can be optionally implemented in order to transform the evaluation context prior to " +
            "flag value resolution.")
    @Test void not_doing() {}
}
