import React, { useEffect, useRef } from 'react';
import DOMPurify from 'dompurify';
import { cn } from '../../services/utils';
import { common, createLowlight } from 'lowlight';
import { toHtml } from 'hast-util-to-html';

const lowlight = createLowlight(common);

interface RichTextViewerProps {
    content: string;
    className?: string;
}

const ALLOWED_TAGS = [
    'p', 'br', 'strong', 'b', 'em', 'i', 'u', 's', 'strike',
    'h1', 'h2', 'h3', 'h4', 'h5', 'h6',
    'ul', 'ol', 'li',
    'blockquote', 'pre', 'code',
    'table', 'thead', 'tbody', 'tr', 'th', 'td',
    'a', 'span', 'div'
];

const ALLOWED_ATTR = ['href', 'target', 'rel', 'class', 'colspan', 'rowspan'];

const sanitizeHtml = (dirty: string): string => {
    return DOMPurify.sanitize(dirty, {
        ALLOWED_TAGS,
        ALLOWED_ATTR,
        ALLOW_DATA_ATTR: false,
        ADD_ATTR: ['target'],
        FORBID_TAGS: ['script', 'style', 'iframe', 'form', 'input', 'button'],
        FORBID_ATTR: ['onerror', 'onload', 'onclick', 'onmouseover', 'onfocus', 'onblur']
    });
};

export const RichTextViewer: React.FC<RichTextViewerProps> = ({ content, className }) => {
    const containerRef = useRef<HTMLDivElement>(null);

    // Sanitize content
    const sanitizedContent = content ? sanitizeHtml(content) : '';

    useEffect(() => {
        if (!sanitizedContent || !containerRef.current) return;
        if (!sanitizedContent.includes('<pre') && !sanitizedContent.includes('<code')) return;

        const highlightBlock = (block: Element) => {
            if (block.classList.contains('hljs')) return;

            const codeText = block.textContent || '';
            if (!codeText.trim()) return;

            try {
                const languageMatch = block.className.match(/language-([\w-]+)/);
                const language = languageMatch?.[1];

                const tree = language && lowlight.registered(language)
                    ? lowlight.highlight(language, codeText)
                    : lowlight.highlightAuto(codeText);

                block.innerHTML = toHtml(tree);
                block.classList.add('hljs');
            } catch (e) {
                console.error('Highlight error:', e);
            }
        };

        // Highlight existing blocks
        containerRef.current.querySelectorAll('pre code').forEach(highlightBlock);

        // New block observer
        const observer = new MutationObserver((mutations) => {
            mutations.forEach((mutation) => {
                mutation.addedNodes.forEach((node) => {
                    if (node instanceof Element) {
                        node.querySelectorAll('pre code').forEach(highlightBlock);
                    }
                });
            });
        });

        observer.observe(containerRef.current, { childList: true, subtree: true });

        return () => observer.disconnect();
    }, [sanitizedContent]);

    if (!content) {
        return <p className="text-muted-foreground italic">Нет описания</p>;
    }

    return (
        <div
            ref={containerRef}
            className={cn(
                // Typography
                "prose prose-sm dark:prose-invert max-w-none break-words",
                // Links
                "prose-a:text-primary prose-a:no-underline hover:prose-a:underline",
                // Remove margins from first and last elements
                "[&>*:first-child]:mt-0 [&>*:last-child]:mb-0",
                // Editor styles
                "prose-pre:p-0 prose-pre:bg-transparent prose-pre:border-none prose-pre:m-0",
                className
            )}
            dangerouslySetInnerHTML={{ __html: sanitizedContent }}
        />
    );
};