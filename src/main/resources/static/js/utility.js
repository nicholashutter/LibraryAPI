/**
 * Utility Functions for Library API
 */

/**
 * Sanitizes a string by removing special characters
 * @param {string} str - The string to sanitize
 * @returns {string} - Sanitized string
 */
function sanitize(str)
{
    if (typeof str !== 'string') return "";
    return str.replace(/[^a-zA-Z0-9\s.,-]/g, "").trim();
}

/**
 * Validates ISBN format
 * @param {string} isbn - The ISBN to validate
 * @returns {boolean} - True if valid ISBN format
 */
function isValidISBN(isbn)
{
    const isbnRegex = /^(?=(?:\D*\d){10}(?:(?:\D*\d){3})?$)[\d-]+$/;
    return isbnRegex.test(isbn);
}

/**
 * Validates date format (DD-MM-YYYY)
 * @param {string} dateStr - The date string to validate
 * @returns {boolean} - True if valid date format
 */
function isValidDate(dateStr)
{
    const dateRegex = /^\d{2}-\d{2}-\d{4}$/;
    if (!dateRegex.test(dateStr)) return false;

    const parts = dateStr.split("-");
    const day = parseInt(parts[0], 10);
    const month = parseInt(parts[1], 10);
    return (month >= 1 && month <= 12 && day >= 1 && day <= 31);
}

/**
 * Formats date from DD-MM-YYYY to ISO format (YYYY-MM-DD)
 * @param {string} dateStr - The date string in DD-MM-YYYY format
 * @returns {string} - Date in ISO format
 */
function formatToISO(dateStr)
{
    const [d, m, y] = dateStr.split("-");
    return `${y}-${m}-${d}`;
}

/**
 * Parses ISO date format to DD-MM-YYYY
 * @param {string} dateStr - The date string in ISO format (YYYY-MM-DD)
 * @returns {string} - Date in DD-MM-YYYY format
 */
function formatFromISO(dateStr)
{
    if (!dateStr) return "";
    const [y, m, d] = dateStr.split("-");
    return `${d}-${m}-${y}`;
}

/**
 * Shows a message in the UI
 * @param {string} text - The message text
 * @param {string} textColor - The text color (hex code)
 * @param {string} bgColor - The background color (hex code)
 */
function showMsg(text, textColor, bgColor)
{
    const messageDiv = document.getElementById('message');
    if (!messageDiv) return;

    messageDiv.style.display = 'block';
    messageDiv.style.color = textColor;
    messageDiv.style.backgroundColor = bgColor;
    messageDiv.innerText = text;
}

/**
 * Colors for messages
 */
const MSG_COLORS = {
    success: { text: '#2e7d32', bg: '#e8f5e9' },
    error: { text: '#c62828', bg: '#ffebee' },
    info: { text: '#1976d2', bg: '#e3f2fd' }
};

/**
 * Shows a success message
 * @param {string} text - The message text
 */
function showSuccessMsg(text)
{
    showMsg(text, MSG_COLORS.success.text, MSG_COLORS.success.bg);
}

/**
 * Shows an error message
 * @param {string} text - The message text
 */
function showErrorMsg(text)
{
    showMsg(text, MSG_COLORS.error.text, MSG_COLORS.error.bg);
}

/**
 * Shows an info message
 * @param {string} text - The message text
 */
function showInfoMsg(text)
{
    showMsg(text, MSG_COLORS.info.text, MSG_COLORS.info.bg);
}

/**
 * Clears inline styles from an element
 * @param {HTMLElement} element - The element to clear
 */
function clearElementStyles(element)
{
    if (element)
    {
        element.style.display = '';
        element.style.color = '';
        element.style.backgroundColor = '';
    }
}
