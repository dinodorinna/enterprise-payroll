import { Button, Card, Checkbox, Descriptions, Empty, Form, FormInstance, Input, InputNumber, message, Modal, PageHeader, Popconfirm, Select, Space, Spin, Table, Tag } from 'antd';
import { ColumnsType } from 'antd/lib/table';
import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { API, APIResponse } from '../util/API';

let globalSetVisible: React.Dispatch<React.SetStateAction<boolean>>;

export const EmployeeDetails = () => {
    const { id } = useParams<any>();

    const [loading, setLoading] = useState<boolean>(true);
    const [apiResponse, setApiResponse] = useState<APIResponse>();
    const [visible, setVisible] = useState(false);
    const [confirmLoading, setConfirmLoading] = useState(false);
    const formRef = React.createRef<FormInstance<any>>();

    globalSetVisible = setVisible;

    useEffect(() => {
        fetchEmployeeData(id, setApiResponse, setLoading);
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
        updateEmployeeSalary(id, values).then(() => {
            setVisible(false);
            setConfirmLoading(false);
            formRef.current.resetFields();

            fetchEmployeeData(id, setApiResponse, setLoading);
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
                    <Card title={<EmployeeDetailHeader data={apiResponse.data} onFired={(empId: number) => {
                        fireEmployee(empId).then(() => {
                            fetchEmployeeData(id, setApiResponse, setLoading);
                        });
                    }} />} bordered={false}>
                        <Table
                            title={EmployeeSalaryTitle}
                            columns={columns}
                            dataSource={apiResponse.data ? apiResponse.data.salaryHistory : []}
                            bordered />
                    </Card>
                }
            </Spin>
            <Modal
                title='Update Salary'
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
                    <Form.Item name={'salary'} label='Salary'>
                        <InputNumber />
                    </Form.Item>
                    <Form.Item name={'role'} label='Role'>
                        <Select>
                            <Select.Option value={0}>CEO</Select.Option>
                            <Select.Option value={1}>CTO</Select.Option>
                            <Select.Option value={2}>COO</Select.Option>
                            <Select.Option value={3}>CFO</Select.Option>
                            <Select.Option value={4}>DEV</Select.Option>
                            <Select.Option value={5}>OPS</Select.Option>
                            <Select.Option value={6}>HR</Select.Option>
                            <Select.Option value={7}>CS</Select.Option>
                            <Select.Option value={8}>OTHER</Select.Option>
                        </Select>
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
            weekday: 'short', // long, short, narrow
            day: 'numeric', // numeric, 2-digit
            year: 'numeric', // numeric, 2-digit
            month: 'long', // numeric, 2-digit, long, short, narrow
            hour: 'numeric', // numeric, 2-digit
            minute: 'numeric', // numeric, 2-digit
            second: 'numeric', // numeric, 2-digit
        }),
        sortDirections: ['descend'],
        defaultSortOrder: 'descend',
    },
    {
        title: 'Salary',
        dataIndex: 'salary',
        key: 'salary',
    },
    {
        title: 'Role',
        dataIndex: 'role',
        key: 'role',
        render: (val: string) => {
            return <Tag color='magenta'>{val}</Tag>;
        },
    },
    {
        title: 'Auditor',
        dataIndex: 'updaterId',
        key: 'updaterId',
    },
];

const fetchEmployeeData = async (empId: number, setApiResponse: React.Dispatch<React.SetStateAction<{}>>, setLoading: React.Dispatch<React.SetStateAction<{}>>) => {
    try {
        const data = await API.get(`http://localhost:8080/employee/${empId}`);

        if (data.status === 200) {
            data.data.salaryHistory = (data.data.salaryHistory as any[]).map((elem, index) => {
                return {
                    key: index,
                    ...elem,
                };
            });
        } else {
            throw new Error();
        }

        setApiResponse(data);
        setLoading(false);
    } catch (e) {
        message.error('Unable to load employee data');
    }
}

const updateEmployeeSalary = async (empId: number, body: any) => {
    try {
        const data = await API.post(`http://localhost:8080/employee/${empId}?a=updateSalary`, body);
        if (data.status !== 200) {
            throw new Error();
        }

        return data;
    } catch (e) {
        message.error('Unable to update employee salary');
        throw e;
    }
}

const fireEmployee = async (empId: number) => {
    try {
        const data = await API.delete(`http://localhost:8080/employee/${empId}`);

        if (data.status === 200) {
            return true;
        } else {
            throw new Error();
        }
    } catch (e) {
        message.error('Unable to fire employee');
        return false;
    }
}

const EmployeeDetailHeader = ({ data, onFired }: any) => {
    return (
        <PageHeader
            ghost={false}
            title={data.englishName}
            subTitle={data.thaiName}
            extra={[
                <Button key='2' disabled={data.deactivated}>Edit</Button>,
                <Popconfirm
                    placement='bottomRight'
                    title='Are you sure?'
                    onConfirm={() => {
                        onFired(data.id);
                    }}
                    okText='Yes'
                    cancelText='No'
                >
                    <Button key='1' disabled={data.deactivated} type='primary' danger>Fired this employee</Button>
                </Popconfirm>,
            ]}>
            <Descriptions size='default' column={2}>
                <Descriptions.Item label='User Id'>{data.id}</Descriptions.Item>
                <Descriptions.Item label='Social Id'>{data.socialId}</Descriptions.Item>
            </Descriptions>
            <Descriptions size='default' column={1}>
                <Descriptions.Item label='Address'>{data.address}</Descriptions.Item>
            </Descriptions>
            <Descriptions size='small' column={2}>
                <Descriptions.Item label='Creation Time'>{new Date(data.joinDate).toLocaleString('en-US', {
                    weekday: 'short', // long, short, narrow
                    day: 'numeric', // numeric, 2-digit
                    year: 'numeric', // numeric, 2-digit
                    month: 'long', // numeric, 2-digit, long, short, narrow
                    hour: 'numeric', // numeric, 2-digit
                    minute: 'numeric', // numeric, 2-digit
                    second: 'numeric', // numeric, 2-digit
                })}</Descriptions.Item>
                <Descriptions.Item label='Update Time'>{new Date(data.lastUpdate).toLocaleString('en-US', {
                    weekday: 'short', // long, short, narrow
                    day: 'numeric', // numeric, 2-digit
                    year: 'numeric', // numeric, 2-digit
                    month: 'long', // numeric, 2-digit, long, short, narrow
                    hour: 'numeric', // numeric, 2-digit
                    minute: 'numeric', // numeric, 2-digit
                    second: 'numeric', // numeric, 2-digit
                })}</Descriptions.Item>
                <Descriptions.Item label='Deactivated'>{data.deactivated.toString()}</Descriptions.Item>
                <Descriptions.Item label='Deactivate Time'>{data.deactivateDate ? new Date(data.deactivateDate).toLocaleString('en-US', {
                    weekday: 'short', // long, short, narrow
                    day: 'numeric', // numeric, 2-digit
                    year: 'numeric', // numeric, 2-digit
                    month: 'long', // numeric, 2-digit, long, short, narrow
                    hour: 'numeric', // numeric, 2-digit
                    minute: 'numeric', // numeric, 2-digit
                    second: 'numeric', // numeric, 2-digit
                }) : 'No deactivated date'}</Descriptions.Item>
            </Descriptions>

            <Descriptions title='Permission' size='small' column={2} style={{ marginTop: '1rem' }}>
                <Descriptions.Item label='Has Payroll Access'>{data.hasPayrollAccess.toString()}</Descriptions.Item>
                <Descriptions.Item label='Payroll Username'>{data.username}</Descriptions.Item>
            </Descriptions>
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
                Employee Salary
            </div>
            <div>
                <Button type='primary' shape='circle' onClick={() => globalSetVisible(true)}>+</Button>
            </div>
        </div>
    );
};
