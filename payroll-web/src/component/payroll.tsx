import { Button, Card, DatePicker, Form, FormInstance, Input, message, Modal, Steps, Table } from 'antd';
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { API, APIResponse } from '../util/API';

export const Payroll = () => {
    const [apiResponse, setApiResponse] = useState<APIResponse>();
    const [visible, setVisible] = useState(false);
    const [confirmLoading, setConfirmLoading] = useState(false);
    const formRef = React.createRef<FormInstance<any>>();

    useEffect(() => {
        fetchEmployeeData(setApiResponse);
    }, []);

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
        createPaylog(values.empId, values).then(() => {
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

    return (
        <>
            <Card title={<PayLogListTitle onCreateButton={showModal} />} bordered={false}>
                <Table
                    columns={columns}
                    dataSource={apiResponse ? apiResponse.data.content : []}
                    bordered />
            </Card>
            <Modal
                title='Create new payout'
                visible={visible}
                onOk={handleOk}
                confirmLoading={confirmLoading}
                onCancel={handleCancel}>
                <Form
                    ref={formRef}
                    {...layout}
                    name='nest-messages'
                    onFinish={onFinish}
                    onFinishFailed={onFinishFailed}>
                    <Form.Item name={'empId'} label='Employee ID' rules={[{ required: true }]}>
                        <Input />
                    </Form.Item>
                    <Form.Item name={'month'} label='Month' rules={[{ required: true }]}>
                        <DatePicker picker={'month'} />
                    </Form.Item>
                    <Form.Item name={'amount'} label='Amount'>
                        <Input />
                    </Form.Item>
                    <Form.Item name={'remark'} label='Remark'>
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

const columns = [
    {
        title: 'Log ID',
        dataIndex: 'id',
        key: 'id',
        render: (text: any, record: any) => <Link to={`/payroll/${record.id}`} target='_blank'>{text}</Link>,
    },
    {
        title: 'Employee ID',
        dataIndex: 'employeeId',
        key: 'employeeId',
        render: (text: any, record: any) => <Link to={`/employee/${text}`} target='_blank'>{text}</Link>,
    },
    {
        title: 'Status',
        dataIndex: 'status',
        key: 'status',
        render: (arr: any[], record: any) => arr[arr.length - 1].status,
    },
    {
        title: 'Month',
        dataIndex: 'timestamp',
        key: 'timestamp',
        render: (val: any) => new Date(val).toLocaleString('en-US', {
            year: 'numeric', // numeric, 2-digit
            month: 'long',
        }),
    },
];

const fetchEmployeeData = async (setApiResponse: React.Dispatch<React.SetStateAction<{}>>) => {
    const data = await API.get('http://localhost:8080/payout');

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

const createPaylog = async (empId: number, body: any) => {
    try {
        return await API.post(`http://localhost:8080/payout/${empId}?a=payout`, body);
    } catch (e) {
        message.error(`Unable to create paylog - ${e}`);
        throw e;
    }
}

const PayLogListTitle = ({ onCreateButton }: any) => (
    <div style={{
        display: 'flex',
        justifyContent: 'space-between',
    }}>
        <div>
            Paylog List
        </div>
        <div>
            <Button type='primary' shape='circle' onClick={() => { onCreateButton(); }}>+</Button>
        </div>
    </div>
);
