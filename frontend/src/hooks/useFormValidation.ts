import {useCallback, useRef, useState} from 'react';

export interface FormValidationResult {
    isValid: boolean;
    errors: Record<string, string>;
}

export const useFormValidation = () => {
    const [forceValidate, setForceValidate] = useState(0);
    const fieldErrorsRef = useRef<Record<string, string>>({});
    const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
    const validatorsRef = useRef<Map<string, () => string>>(new Map());

    const registerFieldError = useCallback((name: string, error: string) => {
        fieldErrorsRef.current[name] = error;
        setFieldErrors(prev => {
            if (prev[name] === error) return prev;
            return { ...prev, [name]: error };
        });
    }, []);

    const registerValidator = useCallback((name: string, validate: () => string): (() => void) => {
        validatorsRef.current.set(name, validate);
        return () => {
            validatorsRef.current.delete(name);
            delete fieldErrorsRef.current[name];
        };
    }, []);

    const clearFieldError = useCallback((name: string) => {
        delete fieldErrorsRef.current[name];
        setFieldErrors(prev => {
            if (!(name in prev)) return prev;
            const newErrors = { ...prev };
            delete newErrors[name];
            return newErrors;
        });
    }, []);

    const validateForm = useCallback((): FormValidationResult => {
        validatorsRef.current.forEach((validate, name) => { fieldErrorsRef.current[name] = validate() });
        setFieldErrors({ ...fieldErrorsRef.current });
        setForceValidate(prev => prev + 1);

        const errors = { ...fieldErrorsRef.current };
        const isValid = Object.values(errors).every(error => !error);

        return { isValid, errors };
    }, []);

    const resetValidation = useCallback(() => {
        fieldErrorsRef.current = {};
        validatorsRef.current.clear();
        setFieldErrors({});
        setForceValidate(0);
    }, []);

    return {
        forceValidate,
        fieldErrors,
        registerFieldError,
        registerValidator,
        clearFieldError,
        validateForm,
        resetValidation
    };
};