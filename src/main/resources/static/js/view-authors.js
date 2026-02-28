/**
 * Authors View Page Logic
 */

let currentAuthorStore = {
    id: null,
    currentIndex: null,
    originalBooks: []
};

/**
 * Fetch and render all authors
 */
async function fetchAuthors()
{
    try
    {
        const authors = await AuthorAPI.getAll();
        const status = document.getElementById('status-msg');

        if (authors.length === 0)
        {
            status.innerText = "No authors found in the database.";
            return;
        }

        status.style.display = 'none';
        renderAuthors(authors);
    } catch (error)
    {
        document.getElementById('status-msg').innerText = "Error loading authors.";
        console.error(error);
    }
}

/**
 * Render authors as cards
 * @param {Array} authors - Array of author objects
 */
function renderAuthors(authors)
{
    const grid = document.getElementById('authors-grid');
    grid.innerHTML = authors.map((author, index) =>
    {
        const titlesStr = (author.books || []).map(b => b.title).join(', ');
        const booksJson = escapeHtmlAttribute(JSON.stringify(author.books || []));

        return `
        <div class="data-card" id="card-${index}">
            <div class="view-mode">
                <div class="author-name">${escapeHtml(author.firstName)} ${escapeHtml(author.lastName)}</div>
                <div class="author-info">
                    <strong>Books:</strong> ${escapeHtml(titlesStr) || 'No titles listed'}
                </div>
                <div style="margin-top: 15px;">
                    <button class="btn-edit" onclick="prepareEditMode(${index}, '${escapeHtmlAttribute(author.firstName)}', '${escapeHtmlAttribute(author.lastName)}', ${booksJson})">
                        Edit Details
                    </button>
                </div>
            </div>
        </div>`;
    }).join('');
}

/**
 * Prepare an author for editing
 */
async function prepareEditMode(index, first, last, booksObj)
{
    const id = await AuthorAPI.getId(first, last);
    if (!id)
    {
        alert("Could not resolve Database ID for " + first + " " + last);
        return;
    }

    currentAuthorStore.id = id;
    currentAuthorStore.currentIndex = index;
    currentAuthorStore.originalBooks = booksObj;

    const titles = booksObj.map(b => b.title).join(', ');
    enterEditMode(index, first, last, titles);
}

/**
 * Enter edit mode for an author
 */
function enterEditMode(index, first, last, titles)
{
    const card = document.getElementById(`card-${index}`);
    card.innerHTML = `
        <div style="display:flex; flex-direction:column; gap:5px;">
            <label>First Name</label>
            <input type="text" id="edit-first-${index}" value="${escapeHtml(first)}">
            
            <label>Last Name</label>
            <input type="text" id="edit-last-${index}" value="${escapeHtml(last)}">
            
            <label>Book Titles (CSV)</label>
            <input type="text" id="edit-books-${index}" value="${escapeHtml(titles)}">
            
            <div style="margin-top:15px; display:flex; gap:10px; flex-wrap: wrap;">
                <button class="btn-save" onclick="saveAuthor(${index})">SAVE</button>
                <button class="btn-cancel" onclick="fetchAuthors()">CANCEL</button>
                <button class="btn-delete" onclick="deleteAuthor(${index})">DELETE</button>
            </div>
        </div>`;
}

/**
 * Save author changes
 */
async function saveAuthor(index)
{
    const newFirst = document.getElementById(`edit-first-${index}`).value;
    const newLast = document.getElementById(`edit-last-${index}`).value;
    const newTitlesStr = document.getElementById(`edit-books-${index}`).value;

    const newTitlesArray = newTitlesStr.split(',').map(t => t.trim()).filter(t => t !== "");

    const finalBooks = newTitlesArray.map(title =>
    {
        const existing = currentAuthorStore.originalBooks.find(b => b.title === title);
        return {
            title: title,
            isbn: existing ? existing.isbn : "",
            publicationDate: existing ? existing.publicationDate : null,
            firstName: newFirst,
            lastName: newLast
        };
    });

    const payload = {
        firstName: newFirst,
        lastName: newLast,
        books: finalBooks
    };

    try
    {
        const response = await AuthorAPI.update(currentAuthorStore.id, payload);
        if (response.ok)
        {
            alert("Author updated successfully!");
            fetchAuthors();
        } else
        {
            alert("Update failed.");
        }
    } catch (error)
    {
        console.error(error);
    }
}

/**
 * Delete an author
 */
async function deleteAuthor(index)
{
    const first = document.getElementById(`edit-first-${index}`).value;
    const last = document.getElementById(`edit-last-${index}`).value;

    if (!confirm(`Delete ${escapeHtml(first)} ${escapeHtml(last)}?`)) return;

    const payload = {
        firstName: first,
        lastName: last,
        books: currentAuthorStore.originalBooks
    };

    try
    {
        const response = await AuthorAPI.delete(payload);
        if (response.ok)
        {
            alert("Deleted.");
            fetchAuthors();
        }
    } catch (error)
    {
        console.error(error);
    }
}

/**
 * Escape HTML special characters for display
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

/**
 * Escape HTML special characters for use in HTML attributes
 */
function escapeHtmlAttribute(text)
{
    return text.replace(/"/g, '&quot;').replace(/'/g, '&#039;');
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', fetchAuthors);
