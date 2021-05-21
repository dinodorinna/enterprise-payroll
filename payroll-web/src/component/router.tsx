import { Col } from 'antd';
import React from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';

import '../static/css/style.css';

import { Employee } from './employee';
import { EmployeeDetails } from './employeeDetail';
import { Home } from './home';
import { Login } from './login';
import { MainMenu } from './mainmenu';
import { Payroll } from './payroll';
import { PayrollDetails } from './payrollDetail';

export const Router = () => {
    return (
        <BrowserRouter>
            <Switch>
                <Route path='/login'>
                    <Login />
                </Route>
                <Route path='/'>
                    <div className='main'>
                        <Col xl={4} lg={5} md={6} sm={7} xs={24}>
                            <MainMenu />
                        </Col>
                        <Col xl={20} lg={19} md={18} sm={17} xs={24}>
                            <Switch>
                                <Route path='/employee'>
                                    <Switch>
                                        <Route path='/employee/:id'>
                                            <EmployeeDetails />
                                        </Route>
                                        <Route path='/employee'>
                                            <Employee />
                                        </Route>
                                    </Switch>
                                </Route>
                                <Route path='/payroll'>
                                    <Switch>
                                        <Route path='/payroll/:id'>
                                            <PayrollDetails />
                                        </Route>
                                        <Route path='/payroll'>
                                            <Payroll />
                                        </Route>
                                    </Switch>
                                </Route>
                                <Route path='/'>
                                    <Home />
                                </Route>
                            </Switch>
                        </Col>
                    </div>
                </Route>
            </Switch>
        </BrowserRouter>
    );
};
