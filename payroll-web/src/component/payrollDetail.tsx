import { Button, Card, Descriptions, Form, FormInstance, Input, message, Modal, PageHeader, Popconfirm, Select, Spin, Table, Tag } from 'antd';
import { ColumnsType } from 'antd/lib/table';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { API, APIResponse } from '../util/API';

let globalSetVisible: React.Dispatch<React.SetStateAction<boolean>>;

export const PayrollDetails = () => {
    const { id } = useParams<any>();

    const [loading, setLoading] = useState<boolean>(true);
    const [apiResponse, setApiResponse] = useState<APIResponse>();
    const [visible, setVisible] = useState(false);
    const [confirmLoading, setConfirmLoading] = useState(false);
    const formRef = React.createRef<FormInstance<any>>();

    globalSetVisible = setVisible;

    useEffect(() => {
        fetchPaylogData(id, setApiResponse, setLoading);
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
        updatePaylogStatus(id, values).then(() => {
            setVisible(false);
            setConfirmLoading(false);
            formRef.current.resetFields();

            fetchPaylogData(id, setApiResponse, setLoading);
        }).catch((e: any) => {
            setConfirmLoading(false);
        });
    };

    const onFinishFailed = () => {
        setConfirmLoading(false);
    };

    return (
        <>
            <Spin tip='Loading...' spinning={loading}>
                {loading ?
                    <Card title='Loading' bordered={false}></Card> :
                    <Card title={<EmployeeDetailHeader data={apiResponse.data} />} bordered={false}>
                        <Table
                            title={EmployeeSalaryTitle}
                            columns={columns}
                            dataSource={apiResponse.data ? apiResponse.data.status : []}
                            pagination={false}
                            bordered />
                    </Card>
                }
            </Spin>
            <Modal
                title='Update paylog status'
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
                    <Form.Item name={'status'} label='Status'>
                        <Select>
                            <Select.Option value={0}>CREATE</Select.Option>
                            <Select.Option value={1}>AUDIT</Select.Option>
                            <Select.Option value={2}>PAYMENT</Select.Option>
                            <Select.Option value={3}>DONE</Select.Option>
                            <Select.Option value={4}>INVALID_BANKID</Select.Option>
                            <Select.Option value={5}>SUSPENDED</Select.Option>
                            <Select.Option value={6}>INVALID</Select.Option>
                            <Select.Option value={7}>DUPLICATED</Select.Option>
                        </Select>
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

const columns: ColumnsType<any> = [
    {
        title: 'Timestamp',
        dataIndex: 'timestamp',
        key: 'timestamp',
        sorter: (a: any, b: any) => {
            const aT = new Date(a.timestamp).getTime();
            const bT = new Date(b.timestamp).getTime();

            return aT - bT;
        },
        render: (val) => new Date(val).toLocaleString('en-US', {
            day: 'numeric', // numeric, 2-digit
            year: 'numeric', // numeric, 2-digit
            month: 'long', // numeric, 2-digit, long, short, narrow
            hour: 'numeric', // numeric, 2-digit
            minute: 'numeric', // numeric, 2-digit
            second: 'numeric', // numeric, 2-digit
        }),
        sortDirections: ['ascend'],
        defaultSortOrder: 'ascend',
    },
    {
        title: 'status',
        dataIndex: 'status',
        key: 'status',
    },
    {
        title: 'Auditor',
        dataIndex: 'updaterId',
        key: 'updaterId',
    },
    {
        title: 'remark',
        dataIndex: 'remark',
        key: 'remark',
    },
];

const fetchPaylogData = async (logId: number, setApiResponse: React.Dispatch<React.SetStateAction<{}>>, setLoading: React.Dispatch<React.SetStateAction<{}>>) => {
    try {
        const data = await API.get(`http://localhost:8080/payout/${logId}`);

        if (data.status === 200) {
            data.data.status = (data.data.status as any[]).map((elem, index) => {
                return {
                    key: index,
                    ...elem,
                };
            });
        } else {
            throw new Error();
        }

        const userData = await API.get(`http://localhost:8080/employee/${data.data.employeeId}`);

        if (userData.status !== 200) {
            throw new Error();
        }

        data.data.employee = userData.data;

        setApiResponse(data);
        setLoading(false);
    } catch (e) {
        message.error('Unable to load employee data');
    }
}

const updatePaylogStatus = async (logId: number, body: any) => {
    try {
        const data = await API.post(`http://localhost:8080/payout/${logId}?a=updatePayStatus`, body);
        if (data.status !== 200) {
            throw new Error();
        }

        return data;
    } catch (e) {
        message.error('Unable to update paylog status');
        throw e;
    }
}

const EmployeeDetailHeader = ({ data }: any) => {
    return (
        <PageHeader
            ghost={false}
            title={`${data.employee.englishName} - #${data.id}`}>
        </PageHeader>
    );
}

const EmployeeSalaryTitle = () => {
    return (
        <div style={{
            display: 'flex',
            justifyContent: 'space-between',
        }}>
            <div style={{
                display: 'flex',
                alignItems: 'center',
            }}>
                Paylog
            </div>
            <div>
                <Button type='primary' shape='circle' onClick={() => globalSetVisible(true)}>+</Button>
            </div>
        </div>
    );
};
