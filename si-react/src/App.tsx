import React, {Component} from 'react';
import ClassDiagram from "./components/ClassDiagram/ClassDiagram";
import UseCaseDiagram from "./components/UseCaseDiagram/UseCaseDiagram";
import Login from "./components/Login/Login";
// @ts-ignore
import {Switch, Route} from 'react-router-dom';
import TreeView from "./components/TreeView/TreeView";
import Rqm from "./components/RQM/Rqm";
// @ts-ignore
class App extends Component {
    render() {
        return (
            <Switch>
                <Route path="/login">
                    <Login />
                </Route>
                <Route path="/dashboard">
                    <div>
                        <TreeView />
                        <Route path="/dashboard/uml">
                            <ClassDiagram />
                        </Route>
                        <Route path="/dashboard/use-case">
                            <UseCaseDiagram />
                        </Route>
                        <Route path="/dashboard/rqm">
                            <Rqm />
                        </Route>
                    </div>
                </Route>
            </Switch>
        );
    }
}

export default App;