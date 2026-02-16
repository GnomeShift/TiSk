import { useState, useCallback, useEffect, useRef } from 'react';

interface UseFieldStateOptions {
    name: string;
    required?: boolean;
    label: string;
    validate?: (value: string) => string;
    onValidationChange?: (name: string, error: string) => void;
    registerValidator?: (name: string, validate: () => string) => () => void;
    forceValidate?: number;
}

export function useFieldState(value: string, options: UseFieldStateOptions) {
    const { name, required = false, label, validate: customValidate, onValidationChange, registerValidator, forceValidate = 0 } = options;
    const [error, setError] = useState('');
    const [touched, setTouched] = useState(false);
    const [focused, setFocused] = useState(false);
    const prevForceValidateRef = useRef(forceValidate);
    const valueRef = useRef(value);
    valueRef.current = value;

    const validateValue = useCallback((val: string): string => {
        if (required && !val.trim()) {
            return `${label} обязательно для заполнения`;
        }
        return customValidate?.(val) ?? '';
    }, [required, label, customValidate]);

    const runValidation = useCallback((val: string): string => {
        const err = validateValue(val);
        setError(err);
        setTouched(true);
        onValidationChange?.(name, err);
        return err;
    }, [validateValue, name, onValidationChange]);

    const validate = useCallback((): string => {
        return runValidation(valueRef.current);
    }, [runValidation]);

    useEffect(() => {
        if (registerValidator) return registerValidator(name, validate);
    }, [registerValidator, name, validate]);

    useEffect(() => {
        if (forceValidate > 0 && forceValidate !== prevForceValidateRef.current) {
            prevForceValidateRef.current = forceValidate;
            if (!registerValidator) runValidation(valueRef.current);
        }
    }, [forceValidate, runValidation, registerValidator]);

    const handleBlur = useCallback(() => {
        setFocused(false);
        runValidation(valueRef.current);
    }, [runValidation]);

    const handleFocus = useCallback(() => {
        setFocused(true);
    }, []);

    const clearError = useCallback(() => {
        setError('');
        onValidationChange?.(name, '');
    }, [name, onValidationChange]);

    const hasError = touched && !!error;
    const isValid = touched && !error && !!value;

    return {
        error,
        touched,
        focused,
        hasError,
        isValid,
        validate,
        handleBlur,
        handleFocus,
        clearError,
        setTouched
    };
}