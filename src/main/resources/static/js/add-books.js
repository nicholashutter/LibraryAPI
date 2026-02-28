/**
 * Add Books Page Logic
 */

/**
 * Initialize the add books form
 */
document.addEventListener('DOMContentLoaded', function ()
{
    const bookForm = document.getElementById('bookForm');
    if (bookForm)
    {
        bookForm.addEventListener('submit', handleBookSubmit);
    }
});

/**
 * Handle book form submission
 */
async function handleBookSubmit(e)
{
    e.preventDefault();
    const messageDiv = document.getElementById('message');
    messageDiv.style.display = 'none';

    const rawTitle = document.getElementById('title').value;
    const rawIsbn = document.getElementById('isbn').value;
    const rawPubDate = document.getElementById('publicationDate').value;
    const rawFirstName = document.getElementById('firstName').value;
    const rawLastName = document.getElementById('lastName').value;

    // Validation
    if (!rawTitle.trim() || !rawFirstName.trim() || !rawLastName.trim())
    {
        showErrorMsg("Title and both Author names are required.");
        return;
    }

    if (!isValidISBN(rawIsbn))
    {
        showErrorMsg("Invalid ISBN format.");
        return;
    }

    if (!isValidDate(rawPubDate))
    {
        showErrorMsg("Invalid Date. Please use DD-MM-YYYY.");
        return;
    }

    const bookData = {
        id: null,
        title: sanitize(rawTitle),
        isbn: rawIsbn.trim(),
        publicationDate: formatToISO(rawPubDate),
        firstName: sanitize(rawFirstName),
        lastName: sanitize(rawLastName)
    };

    try
    {
        const response = await BookAPI.create([bookData]);

        if (response.ok)
        {
            showSuccessMsg("Book added successfully!");
            document.getElementById('bookForm').reset();
        } else
        {
            const errorText = await response.text();
            showErrorMsg("Server Error: " + errorText);
        }
    } catch (error)
    {
        showErrorMsg("Connection error.");
        console.error('Add Book Error:', error);
    }
}
