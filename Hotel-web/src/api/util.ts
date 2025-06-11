export function decodeJwtToken<T = any>(token: string): T | null {
    try {
      const payloadBase64 = token.split('.')[1];
      const decodedPayload = atob(payloadBase64);
      return JSON.parse(decodedPayload) as T;
    } catch (error) {
      console.error('Failed to decode JWT token:', error);
      return null;
    }
  }
  