import { Button, Card, Checkbox, Empty, Form, FormInstance, Input, InputNumber, message, Modal, Space, Table, Tag } from 'antd';
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { API, APIResponse } from '../util/API';

export const Employee = () => {
    const formRef = React.createRef<FormInstance<any>>();
    const [apiResponse, setApiResponse] = useState<APIResponse>();
    const [visible, setVisible] = useState(false);
    const [confirmLoading, setConfirmLoading] = useState(false);

    const showModal = () => {
        setVisible(true);
    };

    const handleOk = () => {
        setConfirmLoading(true);
        formRef.current.submit();
    };

    const handleCancel = () => {
        setVisible(false);
    };

    const onFinish = (values: any) => {
        createEmployee(values).then(() => {
            setVisible(false);
            setConfirmLoading(false);
            formRef.current.resetFields();

            fetchEmployeeData(setApiResponse);
        }).catch((e) => {
            setConfirmLoading(false);
        });
    };

    const onFinishFailed = () => {
        setConfirmLoading(false);
    };


    useEffect(() => {
        fetchEmployeeData(setApiResponse);
    }, []);

    return (
        <>
            <Card title={<EmployeeListTitle onCreateButton={showModal} />} bordered={false}>
                <Table
                    columns={columns}
                    dataSource={apiResponse ? apiResponse.data.content : []}
                    bordered />
            </Card>
            <Modal
                title='Create new employee'
                visible={visible}
                onOk={handleOk}
                confirmLoading={confirmLoading}
                onCancel={handleCancel}>
                <Form
                    ref={formRef}
                    {...layout}
                    name='nest-messages'
                    onFinish={onFinish}
                    onFinishFailed={onFinishFailed}
                    validateMessages={validateMessages}>
                    <Form.Item name={'thaiName'} label='Name (Thai)' rules={[{ required: true }]}>
                        <Input />
                    </Form.Item>
                    <Form.Item name={'englishName'} label='Name (English)' rules={[{ required: true }]}>
                        <Input />
                    </Form.Item>
                    <Form.Item name={'socialId'} label='Social Id' rules={[{ required: true }]}>
                        <Input />
                    </Form.Item>
                    <Form.Item name={'address'} label='Address' rules={[{ required: true }]}>
                        <Input.TextArea />
                    </Form.Item>
                    <Form.Item name={'hasPayrollAccess'} valuePropName='checked' label='Has Payroll Access' rules={[{ type: 'boolean' }]}>
                        <Checkbox />
                    </Form.Item>
                    <Form.Item name={'username'} label='Payroll Username'>
                        <Input />
                    </Form.Item>
                    <Form.Item name={'password'} label='Payroll Password'>
                        <Input />
                    </Form.Item>
                </Form>
            </Modal>
        </>
    );
};

const layout = {
    labelCol: { span: 8 },
    wrapperCol: { span: 16 },
};

const validateMessages = {
    required: '${label} is required!',
    types: {
        email: '${label} is not a valid email!',
        number: '${label} is not a valid number!',
    },
    number: {
        range: '${label} must be between ${min} and ${max}',
    },
};

const columns = [
    {
        title: 'Employee ID',
        dataIndex: 'id',
        key: 'id',
        render: (text: any, record: any) => <Link to={`/employee/${record.id}`} target='_blank'>{text}</Link>,
    },
    {
        title: 'Name (English)',
        dataIndex: 'englishName',
        key: 'englishName',
    },
    {
        title: 'Name (Thai)',
        dataIndex: 'thaiName',
        key: 'thaiName',
    },
    {
        title: 'Address',
        dataIndex: 'address',
        key: 'address',
    },
];

const fetchEmployeeData = async (setApiResponse: React.Dispatch<React.SetStateAction<{}>>) => {
    const data = await API.get('http://localhost:8080/employee');

    if (data.status === 200) {
        data.data.content = data.data.content.map((elem: any) => {
            return {
                key: elem.id,
                ...elem,
            };
        });
    }

    setApiResponse(data);
}

const createEmployee = async (body: any) => {
    try {
        const data = await API.post('http://localhost:8080/employee', body);
        if (data.status !== 200) {
            throw new Error();
        }

        return data;
    } catch (e) {
        message.error('Unable to create new employee');
        throw e;
    }
}

const EmployeeListTitle = ({ onCreateButton }: any) => (
    <div style={{
        display: 'flex',
        justifyContent: 'space-between',
    }}>
        <div>
            Employee List
        </div>
        <div>
            <Button type='primary' shape='circle' onClick={() => { onCreateButton(); }}>+</Button>
        </div>
    </div>
);
