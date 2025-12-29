import React, { useState, useMemo, useEffect, forwardRef, useImperativeHandle } from 'react';

interface PasswordRequirement {
    id: string;
    label: string;
    validate: (value: string) => boolean;
}

export interface PasswordInputRef {
    validate: () => string;
    getValue: () => string;
}

interface PasswordInputProps {
    id: string;
    name: string;
    label: string;
    value: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    placeholder?: string;
    required?: boolean;
    disabled?: boolean;
    autoComplete?: string;
    showRequirements?: boolean;
    forceValidate?: number;
    onValidationChange?: (name: string, error: string) => void;
}

const passwordRequirements: PasswordRequirement[] = [
    {
        id: 'minLength',
        label: 'Минимум 8 символов',
        validate: (value) => value.length >= 8
    },
    {
        id: 'lowercase',
        label: 'Строчная буква (a-z)',
        validate: (value) => /[a-z]/.test(value)
    },
    {
        id: 'uppercase',
        label: 'Заглавная буква (A-Z)',
        validate: (value) => /[A-Z]/.test(value)
    },
    {
        id: 'digit',
        label: 'Цифра (0-9)',
        validate: (value) => /\d/.test(value)
    }
];

const PasswordInput = forwardRef<PasswordInputRef, PasswordInputProps>(({
                                                                            id,
                                                                            name,
                                                                            label,
                                                                            value,
                                                                            onChange,
                                                                            placeholder = '••••••••',
                                                                            required = false,
                                                                            disabled = false,
                                                                            autoComplete,
                                                                            showRequirements = true,
                                                                            forceValidate = 0,
                                                                            onValidationChange
                                                                        }, ref) => {
    const [focused, setFocused] = useState(false);
    const [touched, setTouched] = useState(false);
    const [showPassword, setShowPassword] = useState(false);

    const requirementsStatus = useMemo(() => {
        return passwordRequirements.map(req => ({
            ...req,
            passed: req.validate(value)
        }));
    }, [value]);

    const allPassed = requirementsStatus.every(req => req.passed);
    const passedCount = requirementsStatus.filter(req => req.passed).length;

    const strengthPercent = (passedCount / passwordRequirements.length) * 100;

    const getStrengthLabel = () => {
        if (passedCount === 0) return '';
        if (passedCount === 1) return 'Слабый';
        if (passedCount === 2) return 'Средний';
        if (passedCount === 3) return 'Хороший';
        return 'Надежный';
    };

    const getStrengthColor = () => {
        if (passedCount <= 1) return '#dc3545';
        if (passedCount === 2) return '#ffc107';
        if (passedCount === 3) return '#17a2b8';
        return '#28a745';
    };

    const getError = (): string => {
        if (required && !value) {
            return `${label} обязательно для заполнения`;
        }
        if (value && !allPassed) {
            return 'Пароль не соответствует требованиям';
        }
        return '';
    };

    useImperativeHandle(ref, () => ({
        validate: () => {
            const err = getError();
            setTouched(true);
            return err;
        },
        getValue: () => value
    }));

    // Force validation
    useEffect(() => {
        if (forceValidate > 0) {
            setTouched(true);
            const err = getError();
            onValidationChange?.(name, err);
        }
    }, [forceValidate]);

    // Refresh validation on value change
    useEffect(() => {
        if (touched) {
            const err = getError();
            onValidationChange?.(name, err);
        }
    }, [value, allPassed, touched]);

    const handleBlur = () => {
        setFocused(false);
        setTouched(true);
        const err = getError();
        onValidationChange?.(name, err);
    };

    const handleFocus = () => {
        setFocused(true);
    };

    const hasError = touched && (required && !value || (value && !allPassed));

    const showRequirementsList = showRequirements && (focused || (touched && !allPassed)) && value.length > 0;

    return (
        <div className={`form-group ${hasError ? 'has-error' : ''}`}>
            <label htmlFor={id}>
                {label}
                {required && <span className="required-mark">*</span>}
            </label>

            <div className="input-wrapper password-input-wrapper">
                <input
                    id={id}
                    name={name}
                    type={showPassword ? 'text' : 'password'}
                    value={value}
                    onChange={onChange}
                    onBlur={handleBlur}
                    onFocus={handleFocus}
                    placeholder={placeholder}
                    disabled={disabled}
                    autoComplete={autoComplete}
                    className={`form-control ${hasError ? 'input-error' : ''} ${focused ? 'input-focused' : ''}`}
                    aria-invalid={hasError || undefined}
                />

                <button
                    type="button"
                    className="password-toggle"
                    onClick={() => setShowPassword(!showPassword)}
                    tabIndex={-1}
                    aria-label={showPassword ? 'Скрыть пароль' : 'Показать пароль'}
                >
                    {showPassword ? '🙈' : '👁️'}
                </button>

                {touched && !focused && value && (
                    <span className={`input-icon ${hasError ? 'input-icon-error' : 'input-icon-success'}`}>
                        {hasError ? '✕' : '✓'}
                    </span>
                )}
            </div>

            {/* Error message for empty required field */}
            {touched && required && !value && (
                <span className="error-message" role="alert">
                    <span className="error-icon">⚠</span>
                    {label} обязательно для заполнения
                </span>
            )}

            {/* Password strength indicator */}
            {value.length > 0 && (
                <div className="password-strength">
                    <div className="password-strength-bar">
                        <div
                            className="password-strength-fill"
                            style={{
                                width: `${strengthPercent}%`,
                                backgroundColor: getStrengthColor()
                            }}
                        />
                    </div>
                    <span
                        className="password-strength-label"
                        style={{ color: getStrengthColor() }}
                    >
                        {getStrengthLabel()}
                    </span>
                </div>
            )}

            {/* Password requirements */}
            {showRequirementsList && (
                <div className="password-requirements">
                    {requirementsStatus.map(req => (
                        <div
                            key={req.id}
                            className={`requirement ${req.passed ? 'requirement-passed' : 'requirement-failed'}`}
                        >
                            <span className="requirement-icon">
                                {req.passed ? '✓' : '○'}
                            </span>
                            <span className="requirement-label">{req.label}</span>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
});

PasswordInput.displayName = 'PasswordInput';

export default PasswordInput;