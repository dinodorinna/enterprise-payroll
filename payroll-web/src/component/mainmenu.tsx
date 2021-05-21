import { Menu } from 'antd';
import {
    SettingOutlined, HomeOutlined, UserOutlined,
    MoneyCollectOutlined, RiseOutlined, LogoutOutlined,
    UnorderedListOutlined, ReloadOutlined
} from '@ant-design/icons';
import React from 'react';
import Cookies from 'js-cookie';
import { Link, useHistory, useLocation } from 'react-router-dom';

const { SubMenu } = Menu;

export const MainMenu = () => {
    const history = useHistory();

    const currentToken = Cookies.get('token');
    if (currentToken === undefined || currentToken === null) {
        history.replace('/login');
    }

    const location = useLocation();
    const selectedKey = findSelected(location.pathname);

    return (
        <div style={{
            display: 'flex',
            flexDirection: 'column',
            height: '100%',
        }}>
            <Menu
                style={{
                    height: 'calc(100% - 48px)'
                }}
                defaultSelectedKeys={[selectedKey[1]]}
                defaultOpenKeys={[selectedKey[0]]}
                mode='inline'>
                <Menu.Item key='home' icon={<HomeOutlined />}>
                    <Link to={'/'}>Home</Link>
                </Menu.Item>
                <SubMenu key='emp' icon={<UserOutlined />} title='Employee'>
                    <Menu.Item key='empList' icon={<UnorderedListOutlined />}>
                        <Link to={'/employee'}>Employee List</Link>
                    </Menu.Item>
                </SubMenu>
                <SubMenu key='payroll' icon={<MoneyCollectOutlined />} title='Payroll'>
                    <Menu.Item key='payrollLog' icon={<UnorderedListOutlined />}>
                        <Link to={'/payroll'}>Payroll Log</Link>
                    </Menu.Item>
                </SubMenu>
                <Menu.Item key='settings' icon={<SettingOutlined />}>Settings</Menu.Item>
            </Menu>

            <Menu mode='inline'>
                <Menu.Item key='logout' icon={<LogoutOutlined />} onClick={() => {
                    logout();
                    history.replace('/login');
                }} >Logout</Menu.Item>
            </Menu>
        </div>
    );
};

const findSelected = (path: string) => {
    switch (path) {
        case '/':
            return ['home', 'home'];
        case '/employee':
            return ['emp', 'empList'];
        case '/payroll':
            return ['payroll', 'payrollLog'];
        default:
            return ['', ''];
    }
};

const logout = () => {
    Cookies.remove('token');
};
