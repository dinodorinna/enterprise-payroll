import { Button, Card, Checkbox, Col, Form, Input, message, Row, Space } from 'antd';
import Cookies from 'js-cookie';
import React from 'react';
import { useHistory } from 'react-router-dom';
import { API } from '../util/API';

const layout = {
    labelCol: { span: 8 },
    wrapperCol: { span: 16 },
};
const tailLayout = {
    wrapperCol: { offset: 8, span: 16 },
};

const key = 'updatable';

export const Login = () => {
    const history = useHistory();

    const prepareLogin = (value: any) => {
        message.loading({ content: 'Logging in...', key });

        login(value).then(() => {
            message.success({ content: 'Logged in!', key, duration: 2 });
            history.push('/');
        }).catch(() => {
            message.error({ content: 'Unable to login', key, duration: 2 });
        });
    };

    return (
        <Row>
            <Col xs={0} sm={3} md={5} lg={7} xl={9} />
            <Col xs={24} sm={18} md={14} lg={10} xl={6}>
                <Space direction='vertical' style={{ width: '100%' }}>
                    <Card title='Login'>
                        <Form
                            {...layout}
                            name='basic'
                            onFinish={prepareLogin}>
                            <Form.Item
                                label='Username'
                                name='username'
                                rules={[{ required: true, message: 'Please input your username!' }]}>
                                <Input />
                            </Form.Item>

                            <Form.Item
                                label='Password'
                                name='password'
                                rules={[{ required: true, message: 'Please input your password!' }]}>
                                <Input.Password />
                            </Form.Item>

                            <Form.Item {...tailLayout}>
                                <Button type='primary' htmlType='submit'>Login</Button>
                            </Form.Item>
                        </Form>
                    </Card>
                </Space>
            </Col>
            <Col xs={0} sm={3} md={5} lg={7} xl={9} />
        </Row>
    );
};

const login = async (data: { username: string, password: string }) => {
    const res = await API.post('http://localhost:8080/login', data);

    if (res.status !== 200) {
        throw new Error();
    }

    Cookies.set('token', res.data.token, { expires: 1 });
};
