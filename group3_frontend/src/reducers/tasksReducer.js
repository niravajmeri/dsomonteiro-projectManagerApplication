import {
    GET_AVAILABLE_COLLABORATORS_FOR_TASK_FULLFIELD,
    CHANGE_TASK_FILTER,
    ALL_PROJECT_TASKS_FETCHED
} from '../actions/actions';

export default (state = {}, action) => {
    switch (action.type) {
        case 'FINISHTASKS_FETCHED':
        case 'UNFINISHEDTASKS_FETCHED':
        case 'STANDBYTASKS_FETCHED':
        case 'NOTSTARTED_FETCHED':
        case 'EXPIRED_FETCHED':
        case 'ALLTASKS_FETCHED':
        case 'CANCELLED_FETCHED':
        case ALL_PROJECT_TASKS_FETCHED:
            return {
                ...state,
                itemIsLoading: false,
                tasksList: action.tasks,
                updatedList: action.tasks,
                error: false
            };

        case 'ITEM_LOADING':
            return {
                ...state,
                itemIsLoading: true,
                error: false
            };
        case 'FETCH_HAS_ERRORED':
            return { ...state, itemIsLoading: false, error: true };
        case GET_AVAILABLE_COLLABORATORS_FOR_TASK_FULLFIELD:
            return {
                ...state,
                availableCollaboratorsForTask: action.payload
            };
        case CHANGE_TASK_FILTER:
            return {
                ...state,
                taskFilter: action.filter
            };
        case 'SEARCHTASKS_FETCHED':
            return { ...state, itemIsLoading: false, updatedList: action.updatedList, error: false }
        default:
            return state;
    }
};
