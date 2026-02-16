import React, { useEffect, useState, useRef } from 'react';
import { useEditor, EditorContent, Editor } from '@tiptap/react';
import StarterKit from '@tiptap/starter-kit';
import Placeholder from '@tiptap/extension-placeholder';
import Underline from '@tiptap/extension-underline';
import { Table } from '@tiptap/extension-table';
import TableRow from '@tiptap/extension-table-row';
import TableCell from '@tiptap/extension-table-cell';
import TableHeader from '@tiptap/extension-table-header';
import CodeBlockLowlight from '@tiptap/extension-code-block-lowlight';
import { common, createLowlight } from 'lowlight';
import { Button } from './button';
import { Bold, Italic, Underline as UnderlineIcon, List, ListOrdered, Quote, Undo, Redo,
    Heading2, Code, Table as TableIcon, Trash2, Plus, Rows, Columns } from 'lucide-react';
import { cn } from '../../services/utils';
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem,
    DropdownMenuTrigger, DropdownMenuSeparator } from './dropdown-menu';
import { CharacterCounter } from '../FormInput';

const lowlight = createLowlight(common);

interface RichTextEditorProps {
    value: string;
    onChange: (value: string) => void;
    placeholder?: string;
    label?: string;
    error?: string;
    disabled?: boolean;
    className?: string;
    required?: boolean;
    maxLength?: number;
    hint?: string;
}

interface EditorToolbarProps {
    editor: Editor | null;
    disabled?: boolean;
}

interface ToolbarButtonState {
    bold: boolean;
    italic: boolean;
    underline: boolean;
    heading2: boolean;
    codeBlock: boolean;
    bulletList: boolean;
    orderedList: boolean;
    blockquote: boolean;
    table: boolean;
}

const getToolbarState = (editor: Editor): ToolbarButtonState => ({
    bold: editor.isActive('bold'),
    italic: editor.isActive('italic'),
    underline: editor.isActive('underline'),
    heading2: editor.isActive('heading', { level: 2 }),
    codeBlock: editor.isActive('codeBlock'),
    bulletList: editor.isActive('bulletList'),
    orderedList: editor.isActive('orderedList'),
    blockquote: editor.isActive('blockquote'),
    table: editor.isActive('table'),
});

// Toolbar
const EditorToolbar: React.FC<EditorToolbarProps> = ({ editor, disabled }) => {
    const [state, setState] = useState<ToolbarButtonState | null>(null);

    useEffect(() => {
        if (!editor) return;

        const handleUpdate = () => {
            const newState = getToolbarState(editor);
            setState(prev => {
                if (prev &&
                    prev.bold === newState.bold &&
                    prev.italic === newState.italic &&
                    prev.underline === newState.underline &&
                    prev.heading2 === newState.heading2 &&
                    prev.codeBlock === newState.codeBlock &&
                    prev.bulletList === newState.bulletList &&
                    prev.orderedList === newState.orderedList &&
                    prev.blockquote === newState.blockquote &&
                    prev.table === newState.table
                ) return prev;
                return newState;
            });
        };

        handleUpdate();

        editor.on('selectionUpdate', handleUpdate);
        editor.on('transaction', handleUpdate);

        return () => {
            editor.off('selectionUpdate', handleUpdate);
            editor.off('transaction', handleUpdate);
        };
    }, [editor]);

    if (!editor || !state) return null;

    const isInTable = state.table;

    return (
        <div className="border-b bg-muted/20 p-1 flex flex-wrap gap-1 items-center rounded-t-md">
            <Button
                variant="ghost" size="icon-sm" type="button"
                onClick={() => editor.chain().focus().toggleBold().run()}
                disabled={disabled}
                className={cn(state.bold && "bg-muted text-foreground shadow-inner")}
                title="Жирный"
            >
                <Bold className="h-4 w-4" />
            </Button>
            <Button
                variant="ghost" size="icon-sm" type="button"
                onClick={() => editor.chain().focus().toggleItalic().run()}
                disabled={disabled}
                className={cn(state.italic && "bg-muted text-foreground shadow-inner")}
                title="Курсив"
            >
                <Italic className="h-4 w-4" />
            </Button>
            <Button
                variant="ghost" size="icon-sm" type="button"
                onClick={() => editor.chain().focus().toggleUnderline().run()}
                disabled={disabled}
                className={cn(state.underline && "bg-muted text-foreground shadow-inner")}
                title="Подчеркнутый"
            >
                <UnderlineIcon className="h-4 w-4" />
            </Button>

            <div className="w-px h-6 bg-border mx-1" />

            <Button
                variant="ghost" size="icon-sm" type="button"
                onClick={() => editor.chain().focus().toggleHeading({ level: 2 }).run()}
                disabled={disabled}
                className={cn(state.heading2 && "bg-muted text-foreground shadow-inner")}
                title="Заголовок H2"
            >
                <Heading2 className="h-4 w-4" />
            </Button>
            <Button
                variant="ghost" size="icon-sm" type="button"
                onClick={() => editor.chain().focus().toggleCodeBlock().run()}
                disabled={disabled}
                className={cn(state.codeBlock && "bg-muted text-foreground shadow-inner")}
                title="Код"
            >
                <Code className="h-4 w-4" />
            </Button>

            <div className="w-px h-6 bg-border mx-1" />

            <Button
                variant="ghost" size="icon-sm" type="button"
                onClick={() => editor.chain().focus().toggleBulletList().run()}
                disabled={disabled}
                className={cn(state.bulletList && "bg-muted text-foreground shadow-inner")}
                title="Маркированный список"
            >
                <List className="h-4 w-4" />
            </Button>
            <Button
                variant="ghost" size="icon-sm" type="button"
                onClick={() => editor.chain().focus().toggleOrderedList().run()}
                disabled={disabled}
                className={cn(state.orderedList && "bg-muted text-foreground shadow-inner")}
                title="Нумерованный список"
            >
                <ListOrdered className="h-4 w-4" />
            </Button>
            <Button
                variant="ghost" size="icon-sm" type="button"
                onClick={() => editor.chain().focus().toggleBlockquote().run()}
                disabled={disabled}
                className={cn(state.blockquote && "bg-muted text-foreground shadow-inner")}
                title="Цитата"
            >
                <Quote className="h-4 w-4" />
            </Button>

            <DropdownMenu>
                <DropdownMenuTrigger asChild>
                    <Button
                        variant="ghost" size="icon-sm" type="button"
                        disabled={disabled}
                        className={cn(isInTable && "bg-muted text-foreground shadow-inner")}
                        title="Таблица"
                    >
                        <TableIcon className="h-4 w-4" />
                    </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="start">
                    <DropdownMenuItem
                        onClick={() => editor.chain().focus().insertTable({ rows: 3, cols: 3, withHeaderRow: true }).run()}
                    >
                        <Plus className="mr-2 h-4 w-4" /> Вставить таблицу
                    </DropdownMenuItem>

                    {isInTable && (
                        <>
                            <DropdownMenuSeparator />
                            <DropdownMenuItem
                                onClick={() => editor.chain().focus().addRowAfter().run()}
                                disabled={!editor.can().addRowAfter()}
                            >
                                <Rows className="mr-2 h-4 w-4" /> Добавить строку
                            </DropdownMenuItem>
                            <DropdownMenuItem
                                onClick={() => editor.chain().focus().deleteRow().run()}
                                disabled={!editor.can().deleteRow()}
                            >
                                <Trash2 className="mr-2 h-4 w-4" /> Удалить строку
                            </DropdownMenuItem>
                            <DropdownMenuSeparator />
                            <DropdownMenuItem
                                onClick={() => editor.chain().focus().addColumnAfter().run()}
                                disabled={!editor.can().addColumnAfter()}
                            >
                                <Columns className="mr-2 h-4 w-4" /> Добавить столбец
                            </DropdownMenuItem>
                            <DropdownMenuItem
                                onClick={() => editor.chain().focus().deleteColumn().run()}
                                disabled={!editor.can().deleteColumn()}
                            >
                                <Trash2 className="mr-2 h-4 w-4" /> Удалить столбец
                            </DropdownMenuItem>
                            <DropdownMenuSeparator />
                            <DropdownMenuItem
                                onClick={() => editor.chain().focus().deleteTable().run()}
                                disabled={!editor.can().deleteTable()}
                                className="text-destructive"
                            >
                                <Trash2 className="mr-2 h-4 w-4" /> Удалить таблицу
                            </DropdownMenuItem>
                        </>
                    )}
                </DropdownMenuContent>
            </DropdownMenu>

            <div className="flex-1" />

            <Button
                variant="ghost" size="icon-sm" type="button"
                onClick={() => editor.chain().focus().undo().run()}
                disabled={disabled || !editor.can().chain().focus().undo().run()}
                title="Отменить"
            >
                <Undo className="h-4 w-4" />
            </Button>
            <Button
                variant="ghost" size="icon-sm" type="button"
                onClick={() => editor.chain().focus().redo().run()}
                disabled={disabled || !editor.can().chain().focus().redo().run()}
                title="Повторить"
            >
                <Redo className="h-4 w-4" />
            </Button>
        </div>
    );
};

export const RichTextEditor: React.FC<RichTextEditorProps> = ({
                                                                  value,
                                                                  onChange,
                                                                  placeholder,
                                                                  label,
                                                                  error,
                                                                  disabled,
                                                                  className,
                                                                  required,
                                                                  maxLength,
                                                                  hint
                                                              }) => {
    const [charCount, setCharCount] = useState(0);
    const isInternalUpdateRef = useRef(false);
    const lastValueRef = useRef(value);

    const editor = useEditor({
        extensions: [
            StarterKit.configure({
                codeBlock: false,
            }),
            Underline,
            Table.configure({
                resizable: true,
                allowTableNodeSelection: true,
            }),
            TableRow,
            TableHeader,
            TableCell,
            CodeBlockLowlight.configure({
                lowlight,
            }),
            Placeholder.configure({
                placeholder: placeholder || 'Введите текст...',
                emptyEditorClass: 'cursor-text before:content-[attr(data-placeholder)] before:absolute before:text-muted-foreground before:opacity-50 before-pointer-events-none',
            }),
        ],
        content: value,
        editable: !disabled,
        onUpdate: ({ editor }) => {
            isInternalUpdateRef.current = true;

            const html = editor.isEmpty ? '' : editor.getHTML();
            lastValueRef.current = html;
            setCharCount(html.length);
            onChange(html);

            Promise.resolve().then(() => { isInternalUpdateRef.current = false });
        },
        editorProps: {
            attributes: {
                class: 'prose prose-sm dark:prose-invert max-w-none focus:outline-none min-h-[150px] p-3',
            },
        },
    });

    useEffect(() => {
        if (editor) editor.setEditable(!disabled);
    }, [disabled, editor]);

    useEffect(() => {
        if (!editor) return;

        if (isInternalUpdateRef.current) return;
        if (value === lastValueRef.current) return;
        if (!value || value === '') {
            if (!editor.isEmpty) {
                editor.commands.clearContent();
                setCharCount(0);
            }
            lastValueRef.current = value;
            return;
        }

        lastValueRef.current = value;
        editor.commands.setContent(value);
        setCharCount(value.length);
    }, [value, editor]);

    useEffect(() => {
        if (editor && value) setCharCount(value.length);
    }, [editor]);

    const hasError = !!error;
    const isOverLimit = maxLength ? charCount > maxLength : false;

    return (
        <div className={cn("space-y-1", className)}>
            {/* Label */}
            {label && (
                <label className={cn(
                    "text-sm font-medium leading-none",
                    hasError ? "text-destructive" : "text-foreground"
                )}>
                    {label} {required && <span className="text-destructive font-bold">*</span>}
                </label>
            )}

            {/* Editor */}
            <div className={cn(
                "border rounded-md bg-background ring-offset-background transition-colors",
                "focus-within:ring-2 focus-within:ring-ring focus-within:ring-offset-2",
                (hasError || isOverLimit) ? "border-destructive" : "border-input",
                disabled && "opacity-50 cursor-not-allowed bg-muted"
            )}>
                <EditorToolbar editor={editor} disabled={disabled} />
                <EditorContent editor={editor} />
            </div>

            {/* Error, hint, character counter */}
            <div className="min-h-[1.25rem] flex items-start justify-between gap-2 mt-1">
                <div className="flex-1">
                    {hasError ? (
                        <p className="text-sm font-medium text-destructive">{error}</p>
                    ) : isOverLimit ? (
                        <p className="text-sm font-medium text-destructive">
                            Превышен лимит символов на {charCount - maxLength!}
                        </p>
                    ) : hint ? (
                        <p className="text-sm text-muted-foreground">{hint}</p>
                    ) : null}
                </div>

                {maxLength && (
                    <CharacterCounter current={charCount} max={maxLength} />
                )}
            </div>
        </div>
    );
};