import authenticationReducer from './authenticationReducer';
import { combineReducers } from 'redux';
import { reducer as toastrReducer } from 'react-redux-toastr';
import signUpReducer from './signUpReducer';
import filterReducer from './filterReducer';
import projectTasksReducer from './projectTasksReducer'


const rootReducer = combineReducers({
    authenthication: authenticationReducer,
    toastr: toastrReducer,
    signUp: signUpReducer,
    filterReducer: filterReducer,
    projectTasks: projectTasksReducer
});
export default rootReducer;
