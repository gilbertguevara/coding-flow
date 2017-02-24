import { getHello } from '../src/first-test';
import { expect } from 'chai';

describe('first test', () => {
  it('should return "hello"', () => {
    expect(getHello()).to.equal('hello');
  });
});
