import Axios from 'axios';
import Cookies from 'js-cookie';

export class API {
    public static async get(url: string): Promise<APIResponse> {
        const res = await Axios.get(url, {
            headers: {
                'Authorization': `Bearer ${Cookies.get('token')}`,
                'Content-Type': 'text/json',
            }
        });

        return {
            status: res.status,
            data: res.data,
        }
    }

    public static async post(url: string, body?: any): Promise<APIResponse> {
        const res = await Axios.post(url, body, {
            headers: {
                'Authorization': `Bearer ${Cookies.get('token')}`,
            }
        });

        return {
            status: res.status,
            data: res.data,
        }
    }

    public static async put(url: string, body?: any): Promise<APIResponse> {
        const res = await Axios.put(url, body, {
            headers: {
                'Authorization': `Bearer ${Cookies.get('token')}`,
            }
        });

        return {
            status: res.status,
            data: res.data,
        }
    }

    public static async delete(url: string): Promise<APIResponse> {
        const res = await Axios.delete(url, {
            headers: {
                'Authorization': `Bearer ${Cookies.get('token')}`,
            }
        });

        return {
            status: res.status,
            data: res.data,
        }
    }
}

export interface APIResponse {
    status: number,
    data: any
}
