/**
 * Books View Page Logic
 */

let currentBookStore = {
    id: null,
    originalIsbn: null
};

/**
 * Fetch and render all books
 */
async function fetchBooks()
{
    try
    {
        const books = await BookAPI.getAll();
        const status = document.getElementById('status-msg');

        if (books.length === 0)
        {
            status.innerText = "The library is currently empty.";
            return;
        }

        status.style.display = 'none';
        renderBooks(books);
    } catch (error)
    {
        document.getElementById('status-msg').innerText = "Could not connect to Book API.";
        console.error(error);
    }
}

/**
 * Render books as cards
 * @param {Array} books - Array of book objects
 */
function renderBooks(books)
{
    const grid = document.getElementById('books-grid');
    grid.innerHTML = books.map(book =>
    {
        const key = book.isbn;
        const first = book.firstName || '';
        const last = book.lastName || '';
        const fullName = `${first} ${last}`.trim() || 'Unknown Author';

        return `
        <div class="data-card" id="card-${sanitizeKey(key)}">
            <div class="view-mode">
                <div class="book-title">${escapeHtml(book.title)}</div>
                <div class="book-author">By ${escapeHtml(fullName)}</div>
                <div class="badge book-badge">${sanitizeKey(key) || 'No ISBN'}</div>
                <div style="margin-top: 15px;">
                    <button onclick="prepareBookEdit('${book.id}', '${sanitizeKey(key)}', '${escapeHtml(book.title)}', '${escapeHtml(first)}', '${escapeHtml(last)}', '${book.publicationDate || ''}')" 
                            class="btn-edit">
                        EDIT
                    </button>
                </div>
            </div>
        </div>`;
    }).join('');
}

/**
 * Prepare book for editing
 * @param {string} id - Book ID
 * @param {string} isbn - Book ISBN
 * @param {string} title - Book title
 * @param {string} firstName - Author first name
 * @param {string} lastName - Author last name
 * @param {string} pubDate - Publication date
 */
async function prepareBookEdit(id, isbn, title, firstName, lastName, pubDate)
{
    currentBookStore.id = id;
    currentBookStore.originalIsbn = isbn;
    enterBookEditMode(isbn, title, firstName, lastName, pubDate);
}

/**
 * Enter edit mode for a book
 */
function enterBookEditMode(isbn, title, firstName, lastName, pubDate)
{
    const cardKey = sanitizeKey(isbn);
    const card = document.getElementById(`card-${cardKey}`);
    const viewModeDiv = card.querySelector('.view-mode');

    viewModeDiv.innerHTML = `
    <div style="display:flex; flex-direction:column; gap:8px;">
        <label style="font-size:0.6rem;">Title</label>
        <input type="text" id="edit-title-${cardKey}" value="${escapeHtml(title)}" style="width:90%; padding:4px;">
        
        <div style="display:flex; gap:5px; width:90%;">
            <div style="flex:1;">
                <label style="font-size:0.6rem;">First Name</label>
                <input type="text" id="edit-first-${cardKey}" value="${escapeHtml(firstName)}" style="width:100%; padding:4px;">
            </div>
            <div style="flex:1;">
                <label style="font-size:0.6rem;">Last Name</label>
                <input type="text" id="edit-last-${cardKey}" value="${escapeHtml(lastName)}" style="width:100%; padding:4px;">
            </div>
        </div>
        
        <label style="font-size:0.6rem;">Pub Date</label>
        <input type="date" id="edit-date-${cardKey}" value="${pubDate}" style="width:90%; padding:4px;">
        
        <div style="margin-top:10px; display:flex; gap:10px; align-items: center;">
            <button onclick="saveBook('${cardKey}')" class="btn-save">SAVE</button>
            <button onclick="fetchBooks()" class="btn-cancel">CANCEL</button>
            <button onclick="deleteBook('${cardKey}')" class="btn-delete">DELETE</button>
        </div>
    </div>`;
}

/**
 * Delete a book
 */
async function deleteBook(cardKey)
{
    const title = document.getElementById(`edit-title-${cardKey}`).value;

    if (!currentBookStore.id)
    {
        alert("Session Error: Missing Book ID. Please try again.");
        return;
    }

    const isConfirmed = confirm(`WARNING: You are about to delete "${title}". This action cannot be undone. Do you wish to proceed?`);
    if (!isConfirmed) return;

    try
    {
        const response = await BookAPI.delete(currentBookStore.id);
        if (response.ok)
        {
            alert("Book deleted successfully.");
            currentBookStore.id = null;
            currentBookStore.originalIsbn = null;
            fetchBooks();
        } else
        {
            const errorText = await response.text();
            alert("Failed to delete book: " + errorText);
        }
    } catch (error)
    {
        console.error("Delete Error:", error);
        alert("Connection error while trying to delete.");
    }
}

/**
 * Save book changes
 */
async function saveBook(cardKey)
{
    if (!currentBookStore.id)
    {
        alert("Session Error: Missing Book ID. Please try again.");
        return;
    }

    const payload = {
        id: currentBookStore.id,
        title: document.getElementById(`edit-title-${cardKey}`).value,
        isbn: currentBookStore.originalIsbn,
        publicationDate: document.getElementById(`edit-date-${cardKey}`).value,
        firstName: document.getElementById(`edit-first-${cardKey}`).value,
        lastName: document.getElementById(`edit-last-${cardKey}`).value
    };

    try
    {
        const response = await BookAPI.update(currentBookStore.id, payload);
        if (response.ok)
        {
            alert("Book updated successfully!");
            currentBookStore.id = null;
            fetchBooks();
        } else
        {
            const errorText = await response.text();
            alert("Failed to save book: " + errorText);
        }
    } catch (error)
    {
        console.error("Save Error:", error);
        alert("Connection error.");
    }
}

/**
 * Sanitize key for use in HTML IDs (removes special characters)
 */
function sanitizeKey(key)
{
    return key.replace(/[^a-zA-Z0-9_-]/g, '_');
}

/**
 * Escape HTML special characters
 */
function escapeHtml(text)
{
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    return text.replace(/[&<>"']/g, m => map[m]);
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', fetchBooks);
