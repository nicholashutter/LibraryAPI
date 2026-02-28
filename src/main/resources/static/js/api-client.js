/**
 * API Client for Library API
 */

/**
 * Generic fetch wrapper with error handling
 * @param {string} url - The API endpoint URL
 * @param {Object} options - Fetch options (method, headers, body)
 * @returns {Promise} - Response promise
 */
async function apiCall(url, options = {})
{
    try
    {
        const response = await fetch(url, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });
        return response;
    } catch (error)
    {
        console.error('API Call Error:', error);
        throw error;
    }
}

/**
 * GET request handler
 * @param {string} url - The API endpoint URL
 * @returns {Promise<Object>} - Parsed JSON response
 */
async function apiGet(url)
{
    const response = await apiCall(url, { method: 'GET' });
    if (!response.ok) throw new Error(`GET ${url} failed with status ${response.status}`);
    return response.json();
}

/**
 * GET request returning text
 * @param {string} url - The API endpoint URL
 * @returns {Promise<string>} - Text response
 */
async function apiGetText(url)
{
    const response = await apiCall(url, { method: 'GET' });
    if (!response.ok) throw new Error(`GET ${url} failed with status ${response.status}`);
    return response.text();
}

/**
 * POST request handler
 * @param {string} url - The API endpoint URL
 * @param {Object} data - The data to post
 * @returns {Promise<Object>} - Parsed JSON response
 */
async function apiPost(url, data)
{
    const response = await apiCall(url, {
        method: 'POST',
        body: JSON.stringify(data)
    });
    return response;
}

/**
 * PUT request handler
 * @param {string} url - The API endpoint URL
 * @param {Object} data - The data to update
 * @returns {Promise<Object>} - Response
 */
async function apiPut(url, data)
{
    const response = await apiCall(url, {
        method: 'PUT',
        body: JSON.stringify(data)
    });
    return response;
}

/**
 * DELETE request handler
 * @param {string} url - The API endpoint URL
 * @param {Object} data - Optional data for delete body
 * @returns {Promise<Object>} - Response
 */
async function apiDelete(url, data = null)
{
    const options = { method: 'DELETE' };
    if (data)
    {
        options.body = JSON.stringify(data);
    }
    const response = await apiCall(url, options);
    return response;
}

/**
 * Authors API Endpoints
 */
const AuthorAPI = {
    baseUrl: '/authors',

    /**
     * Fetch all authors
     * @returns {Promise<Array>} - Array of authors
     */
    getAll: async function ()
    {
        return apiGet(this.baseUrl);
    },

    /**
     * Get author ID by first and last name
     * @param {string} firstName - Author's first name
     * @param {string} lastName - Author's last name
     * @returns {Promise<string>} - Author ID
     */
    getId: async function (firstName, lastName)
    {
        try
        {
            const url = `${this.baseUrl}/${encodeURIComponent(firstName)}/${encodeURIComponent(lastName)}`;
            const response = await apiCall(url, { method: 'GET' });
            if (!response.ok) throw new Error('Author not found');
            const id = await response.text();
            return id.replace(/"/g, '');
        } catch (error)
        {
            console.error("Author ID Fetch Error:", error);
            return null;
        }
    },

    /**
     * Create new authors
     * @param {Array} authors - Array of author objects
     * @returns {Promise<Object>} - Response
     */
    create: async function (authors)
    {
        return apiPost(this.baseUrl, authors);
    },

    /**
     * Update an author
     * @param {string} id - Author ID
     * @param {Object} author - Author data
     * @returns {Promise<Object>} - Response
     */
    update: async function (id, author)
    {
        return apiPut(`${this.baseUrl}/${id}`, author);
    },

    /**
     * Delete an author
     * @param {string} id - Author ID
     * @returns {Promise<Object>} - Response
     */
    delete: async function (id)
    {
        return apiDelete(`${this.baseUrl}/${id}`);
    }
};

/**
 * Books API Endpoints
 */
const BookAPI = {
    baseUrl: '/books',

    /**
     * Fetch all books
     * @returns {Promise<Array>} - Array of books
     */
    getAll: async function ()
    {
        return apiGet(this.baseUrl);
    },

    /**
     * Get book ID by ISBN
     * @param {string} isbn - Book ISBN
     * @returns {Promise<string>} - Book ID
     */
    getId: async function (isbn)
    {
        try
        {
            const response = await apiCall(`${this.baseUrl}/${isbn}`, { method: 'GET' });
            if (!response.ok) throw new Error('Book not found');
            const data = await response.text();
            return data.replace(/"/g, '');
        } catch (error)
        {
            console.error("Book ID Fetch Error:", error);
            return null;
        }
    },

    /**
     * Create new books
     * @param {Array} books - Array of book objects
     * @returns {Promise<Object>} - Response
     */
    create: async function (books)
    {
        return apiPost(this.baseUrl, books);
    },

    /**
     * Update a book
     * @param {string} id - Book ID
     * @param {Object} book - Book data
     * @returns {Promise<Object>} - Response
     */
    update: async function (id, book)
    {
        return apiPut(`${this.baseUrl}/${id}`, book);
    },

    /**
     * Delete a book
     * @param {string} id - Book ID
     * @returns {Promise<Object>} - Response
     */
    delete: async function (id)
    {
        return apiDelete(`${this.baseUrl}/${id}`);
    }
};
