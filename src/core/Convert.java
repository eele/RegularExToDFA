package core;

import java.util.Stack;

import data.StateMatrix;

public class Convert {

	/**
	 * 将正则表达式转为DFA
	 * 
	 * @param text
	 * @return
	 */
	public StateMatrix toDFA(String text) {
		StateMatrix stateMatrix = new StateMatrix();
		String postfix = reToPostfix(text);
		
		return stateMatrix;
	}

	/**
	 * 正则表达式改为后缀表达式
	 * 
	 * @param text
	 * @return
	 */
	private String reToPostfix(String text) {
		int i = 0;
		char c, pc;
		StringBuilder sb = new StringBuilder(text);
		sb.append('\0');

		pc = sb.charAt(i);
		c = sb.charAt(++i);
		while (sb.charAt(i) != '\0') // 添加&
		{
			if (((pc == ')' && c == '(')) || (!isOperator(pc) && !isOperator(c)) || (!isOperator(pc) && c == '(')
					|| (pc == ')' && !isOperator(c)) || (pc == '*' && !isOperator(c)) || (pc == '*' && c == '(')) {// 四种情况需要加&
				sb.insert(i, "&");
			}
			pc = sb.charAt(i++);
			c = sb.charAt(i);
		}
		sb.append("!");
		sb.replace(sb.indexOf("\0"), sb.indexOf("\0") + 1, "");
		
		// 转化为后缀表达式
		String l = "";
		Stack<Character> stack = new Stack<Character>();
		
		stack.push('$');
		char tempc, tempc2;

		for (i = 0; i<sb.length(); i++)
		{
			tempc = sb.charAt(i);
			if (isOperator(tempc))
			{
				switch (tempc)
				{
				case '(': stack.push(tempc); break;
				case ')':
					while (stack.peek() != '(')
					{
						tempc2 = stack.peek();
						stack.pop();
						l += tempc2;
					}
					stack.pop();
					break;
				default:
					tempc2 = stack.peek();
					while (tempc2 != '('&&Operator_Less_Than(tempc, tempc2))
					{
						tempc2 = stack.peek();
						stack.pop();
						l += tempc2;
						tempc2 = stack.peek();
					}
					stack.push(tempc);
					break;
				}
			}
			else
				l += tempc;
		}

		return l;
	}

	private boolean isOperator(char c) {
		switch (c) {
		case '|':
		case '&':
		case '(':
		case ')':
		case '!':
		case '*':
			return true;
		default:
			return false;
		}
	}
	
	private boolean Operator_Less_Than(char t1, char t2)
	{
		int temp1 = getOperatorNumber(t1);
		int temp2 = getOperatorNumber(t2);
		if (temp1 <= temp2)
			return true;
		return false;
	}
	
	private int getOperatorNumber(char t1)
	{
		switch (t1)
		{
		case '$': return 0;
		case '!': return 1;
		case ')': return 2;
		case '|': return 3;
		case '&': return 4;
		case '*': return 5;
		case '(': return 6;
		default: return 7;
		}
	}
}
