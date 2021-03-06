import React from 'react';
import { shallow } from 'enzyme';
import MediumButton from "../components/button/mediumButton"


describe('MediumButton', () => {
    it('should be defined', () => {
        expect(MediumButton).toBeDefined();
    });
    it('should render correctly', () => {
        const tree = shallow(
            <MediumButton name='button test' />

        );
        expect(tree).toMatchSnapshot();
    });
    it('should have a button value', () => {
        const tree = shallow(
          <MediumButton name='button test' />
        );
        expect(typeof(tree.find('.mediumButton').getElement().props['className'])).toBe('string');
      });
  
    

})