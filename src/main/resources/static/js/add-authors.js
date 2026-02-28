/**
 * Add Authors Page Logic
 */

let booksArray = [];

/**
 * Initialize the add authors form
 */
document.addEventListener('DOMContentLoaded', function ()
{
    const authorForm = document.getElementById('authorForm');
    if (authorForm)
    {
        authorForm.addEventListener('submit', handleAuthorSubmit);
    }
});

/**
 * Add a book to the books array and re-render
 */
function addBookToList()
{
    const title = document.getElementById('tempTitle').value;
    const isbn = document.getElementById('tempIsbn').value;
    const pubDate = document.getElementById('tempDate').value;

    const fName = document.getElementById('firstName').value;
    const lName = document.getElementById('lastName').value;

    if (!title || !isbn)
    {
        alert("Please enter at least a Title and ISBN");
        return;
    }

    const bookObj = {
        id: null,
        title: sanitize(title),
        isbn: sanitize(isbn),
        publicationDate: pubDate,
        firstName: sanitize(fName),
        lastName: sanitize(lName)
    };

    booksArray.push(bookObj);
    renderBooks();

    // Clear book-specific inputs
    document.getElementById('tempTitle').value = '';
    document.getElementById('tempIsbn').value = '';
    document.getElementById('tempDate').value = '';
}

/**
 * Remove a book from the array at given index
 */
function removeBook(index)
{
    booksArray.splice(index, 1);
    renderBooks();
}

/**
 * Render queued books as items
 */
function renderBooks()
{
    const container = document.getElementById('queuedBooks');
    container.innerHTML = '';
    booksArray.forEach((book, index) =>
    {
        container.innerHTML += `
            <div class="book-item">
                <span><strong>${escapeHtml(book.title)}</strong> (ISBN: ${escapeHtml(book.isbn)})</span>
                <button type="button" class="remove-btn" onclick="removeBook(${index})">✕</button>
            </div>
        `;
    });
}

/**
 * Handle author form submission
 */
async function handleAuthorSubmit(e)
{
    e.preventDefault();

    const fName = sanitize(document.getElementById('firstName').value);
    const lName = sanitize(document.getElementById('lastName').value);

    const finalBooks = booksArray.map(book => ({
        ...book,
        firstName: fName,
        lastName: lName
    }));

    const authorData = {
        firstName: fName,
        lastName: lName,
        books: finalBooks
    };

    try
    {
        const response = await AuthorAPI.create([authorData]);

        if (response.ok)
        {
            showSuccessMsg("Author and " + booksArray.length + " books saved!");
            booksArray = [];
            renderBooks();
            document.getElementById('authorForm').reset();
        } else
        {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Server error');
        }
    } catch (error)
    {
        showErrorMsg("Error: " + error.message);
        console.error('Add Author Error:', error);
    }
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
