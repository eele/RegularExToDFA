package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
		Map<String, Object> map = getFirstposAndFollowpos(postfix);
//		stateMatrix.setMatrix(createStateMatrix(map));
		createStateMatrix(map);
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
		String postfix = "";
		Stack<Character> stack = new Stack<Character>();

		stack.push('$');
		char tempc, tempc2;

		for (i = 0; i < sb.length(); i++) {
			tempc = sb.charAt(i);
			if (isOperator(tempc)) {
				switch (tempc) {
				case '(':
					stack.push(tempc);
					break;
				case ')':
					while (stack.peek() != '(') {
						tempc2 = stack.peek();
						stack.pop();
						postfix += tempc2;
					}
					stack.pop();
					break;
				default:
					tempc2 = stack.peek();
					while (tempc2 != '(' && Operator_Less_Than(tempc, tempc2)) {
						tempc2 = stack.peek();
						stack.pop();
						postfix += tempc2;
						tempc2 = stack.peek();
					}
					stack.push(tempc);
					break;
				}
			} else
				postfix += tempc;
		}
		postfix = postfix + "#&";

		return postfix;
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

	private boolean Operator_Less_Than(char t1, char t2) {
		int temp1 = getOperatorNumber(t1);
		int temp2 = getOperatorNumber(t2);
		if (temp1 <= temp2)
			return true;
		return false;
	}

	private int getOperatorNumber(char t1) {
		switch (t1) {
		case '$':
			return 0;
		case '!':
			return 1;
		case ')':
			return 2;
		case '|':
			return 3;
		case '&':
			return 4;
		case '*':
			return 5;
		case '(':
			return 6;
		default:
			return 7;
		}
	}

	/**
	 * 根据后缀表达式得到根节点的firstpos集合和各结点的followpos集合
	 * 
	 * @param postfix
	 * @return
	 */
	private Map<String, Object> getFirstposAndFollowpos(String postfix) {
		Map<String, Object> map = new HashMap<String, Object>();
		int length = postfix.length();
		boolean[] nullable = new boolean[length];
		int[] position = new int[length];// 标识字符位置
		int[][] firstpos = new int[length][length];
		int[][] lastpos = new int[length][length];
		int[][] followpos = new int[length][length];
		int pos = 1;
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				firstpos[i][j] = lastpos[i][j] = followpos[i][j] = 0;
			}
		}
		for (int i = 0; i < length; i++) {
			if (Isalpha(postfix.charAt(i)) || postfix.charAt(i) == '#') {
				position[i] = pos;
				pos++;
				nullable[i] = false;
				firstpos[i][0] = position[i];
				lastpos[i][0] = position[i];
			} else {
				position[i] = 0;// 运算符的position设为0
				if (postfix.charAt(i) == '|') {
					nullable[i] = (nullable[i - 2] || nullable[i - 1]);

					int j = 0;
					while (firstpos[i - 1][j] != 0) {
						firstpos[i][j] = firstpos[i - 1][j];
						lastpos[i][j] = firstpos[i - 1][j];
						j++;
					}
					int k = 0;
					int m;
					while (firstpos[i - 2][k] != 0) {
						for (m = 0; m < j; m++) {
							if (firstpos[i][m] == firstpos[i - 2][k])
								break;
						}
						if (m == j) {
							firstpos[i][j] = firstpos[i - 2][k];
							lastpos[i][j] = firstpos[i - 2][k];
							j++;
						}
						k++;
					}

				} else if (postfix.charAt(i) == '*') {
					nullable[i] = true;

					int j = 0;
					while (firstpos[i - 1][j] != 0) {
						firstpos[i][j] = firstpos[i - 1][j];
						lastpos[i][j] = firstpos[i - 1][j];
						j++;
					}
				} else // 连接符.
				{
					nullable[i] = (nullable[i - 2] && nullable[i - 1]);

					if (nullable[i - 2] == true) {
						int j = 0;
						int m;
						while (firstpos[i - 1][j] != 0) {
							firstpos[i][j] = firstpos[i - 1][j];
							j++;
						}
						int k = 0;
						while (firstpos[i - 2][k] != 0) {
							for (m = 0; m < j; m++) {
								if (firstpos[i][m] == firstpos[i - 2][k])
									break;
							}
							if (m == j) {
								firstpos[i][j] = firstpos[i - 2][k];
								j++;
							}
							k++;
						}
					} else {
						int j = 0;
						while (firstpos[i - 2][j] != 0) {
							firstpos[i][j] = firstpos[i - 2][j];
							j++;
						}
					}

					if (nullable[i - 1]) {
						int j = 0;
						while (firstpos[i - 1][j] != 0) {
							lastpos[i][j] = firstpos[i - 1][j];
							j++;
						}
						int k = 0;
						int m;
						while (firstpos[i - 2][k] != 0) {
							for (m = 0; m < j; m++) {
								if (lastpos[i][m] == firstpos[i - 2][k])
									break;
							}
							if (m == j) {
								lastpos[i][j] = firstpos[i - 2][k];
								j++;
							}
							k++;
						}
					} else {
						int j = 0;
						while (firstpos[i - 1][j] != 0) {
							lastpos[i][j] = firstpos[i - 1][j];
							j++;
						}
					}

				}
			}
		}

		Set<Integer> firstposSet = new HashSet<Integer>();
		int j = 0;
		while (firstpos[length - 1][j] != 0) // 添加后缀式根节点的firstpos集合
		{
			firstposSet.add(firstpos[length - 1][j]);
			j++;
		}
		map.put("firstpos", firstposSet);

		for (int i = 0; i < length; i++) {
			if (postfix.charAt(i) == '&') {
				int m = 0;
				while (lastpos[i - 2][m] != 0) {
					int k;
					for (k = 0; k < length; k++) {
						if (followpos[lastpos[i - 2][m]][k] == 0)
							break;
					}
					int n = 0;
					while (firstpos[i - 1][n] != 0) {
						followpos[lastpos[i - 2][m]][k] = firstpos[i - 1][n];
						n++;
						k++;
					}
					m++;
				}
			}
			if (postfix.charAt(i) == '*') {
				int m = 0;
				while (lastpos[i][m] != 0) {
					int n;
					for (n = 0; n < length; n++) {
						if (followpos[lastpos[i][m]][n] == 0)
							break;
					}
					int k = 0;
					while (firstpos[i][k] != 0) {
						followpos[lastpos[i][m]][n] = firstpos[i][k];
						k++;
						n++;
					}
					m++;
				}
			}
		}

		Map<Integer, Character> labelMap = new HashMap<Integer, Character>();
		for (int i = 0; i < length; i++) { // 获取字符的编号表
			if (position[i] != 0) {
				labelMap.put(position[i], postfix.charAt(i));
			}
		}
		map.put("label", labelMap);

		Map<Integer, Set<Integer>> followposMap = new HashMap<Integer, Set<Integer>>();
		for (int i = 0; i < length; i++) {
			if (position[i] != 0) {
				int k = 0;
				Set<Integer> followposSet = new HashSet<Integer>();
				while (followpos[position[i]][k] != 0) {
					followposSet.add(followpos[position[i]][k]);
					k++;
				}
				followposMap.put(position[i], followposSet);
			}
		}
		map.put("followpos", followposMap);

		return map;
	}

	private boolean Isalpha(char c) {
		if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
			return true;
		else
			return false;
	}

	/**
	 * 创建状态矩阵表
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int[][] createStateMatrix(Map<String, Object> map) {
		Map<Integer, Character> labelMap = (Map<Integer, Character>) map.get("label");
		Set<Character> set = new HashSet<Character>();
		for (Character ch : labelMap.values()) { // 获取不重复的字符集
			if (ch != '#') {
				set.add(ch);
			}
		}

		List<Set<Integer>> stateList = new ArrayList<Set<Integer>>();
		List<List<Object>> tran = new ArrayList<List<Object>>();
		Map<Integer, Set<Integer>> followposMap = (Map<Integer, Set<Integer>>) map.get("followpos");
		Stack<Set<Integer>> stateStack = new Stack<Set<Integer>>();
		stateStack.push((Set<Integer>) map.get("firstpos"));
		while (!stateStack.isEmpty()) {
			Set<Integer> state = stateStack.pop();
			stateList.add(state);
			for (Character ch : set) {
				Set<Integer> tmpSet = new HashSet<Integer>();
				for (int i : state) {
					if (labelMap.get(i) == ch) {
						tmpSet.addAll(followposMap.get(i));
					}
				}
				if(!tmpSet.isEmpty()) {
					int eq = 0;
					for(Set<Integer> set1 : stateList) {
						if(isSetEqual(set1, tmpSet)) {
							eq = 1;
							break;
						}
					}
					if (eq == 0) {
						stateStack.push(tmpSet);
					}
					List<Object> rel = new ArrayList<Object>();
					rel.add(state);
					rel.add(ch);
					rel.add(tmpSet);
					tran.add(rel);
				}
			}
		}
		System.out.println(tran);
		return null;
	}

	private boolean isSetEqual(@SuppressWarnings("rawtypes") Set set1, @SuppressWarnings("rawtypes") Set set2) {

		if (set1 == null && set2 == null) {
			return true; // Both are null
		}

		if (set1 == null || set2 == null || set1.size() != set2.size() || set1.size() == 0 || set2.size() == 0) {
			return false;
		}

		@SuppressWarnings("rawtypes")
		Iterator ite2 = set2.iterator();

		boolean isFullEqual = true;

		while (ite2.hasNext()) {
			if (!set1.contains(ite2.next())) {
				isFullEqual = false;
			}
		}

		return isFullEqual;
	}
}
