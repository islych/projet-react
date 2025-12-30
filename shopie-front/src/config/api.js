// API Configuration for Shopie Backend
export const API_CONFIG = {
  BASE_URL: 'http://20.30.0.202:8081/api', // Your computer's IP address
  // Alternative for Android emulator: 'http://10.0.2.2:8081/api'
  
  ENDPOINTS: {
    // Auth
    LOGIN: '/auth/login',
    REGISTER: '/auth/register',
    
    // Products
    PRODUCTS: '/products',
    PRODUCT_SEARCH: '/products/search',
    
    // Cart
    CART: '/cart',
    
    // Orders
    ORDERS: '/orders',
    
    // Payments
    PAYMENTS: '/payments',
  }
};

// Default headers for API requests
export const getHeaders = (token = null) => {
  const headers = {
    'Content-Type': 'application/json',
  };
  
  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }
  
  return headers;
};

// API request helper with better error handling
export const apiRequest = async (endpoint, options = {}) => {
  const url = `${API_CONFIG.BASE_URL}${endpoint}`;
  
  try {
    console.log(`API Request: ${options.method || 'GET'} ${url}`);
    
    const response = await fetch(url, {
      ...options,
      headers: {
        ...getHeaders(),
        ...options.headers,
      },
    });

    if (!response.ok) {
      let errorMessage = `HTTP error! status: ${response.status}`;
      try {
        const errorData = await response.text();
        if (errorData) {
          errorMessage = errorData;
        }
      } catch (e) {
        // If we can't parse the error, use the status
      }
      throw new Error(errorMessage);
    }

    const data = await response.json();
    console.log(`API Response: ${url}`, data);
    return data;
  } catch (error) {
    console.error('API Request Error:', error);
    throw error;
  }
};