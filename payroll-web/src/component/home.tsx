import { Empty } from 'antd';
import React from 'react';
import '../static/css/style.css';

export const Home = () => {
    return (
        <div style={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            height: '100%',
        }}>
            <Empty description={'Empty Dashboard'} />
        </div >
    );
};
