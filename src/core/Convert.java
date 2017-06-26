package core;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import data.StateMatrix;

/**
 * 正规表达式到DFA的转换类
 * @author ele
 *
 */
public class Convert {

	/**
	 * 将正规表达式转为DFA
	 * 
	 * @param text
	 * @return
	 */
	public StateMatrix toDFA(String text) throws EmptyStackException {
		String postfix = reToPostfix(text);
		Map<String, Object> map = getFirstposAndFollowpos(postfix);
		StateMatrix stateMatrix = createStateMatrix(map);
		return stateMatrix;
	}

	/**
	 * 正规表达式改为后缀表达式
	 * 
	 * @param text
	 * @return
	 */
	public String reToPostfix(String text) throws EmptyStackException {
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

	/**
	 * 检查字符是否为运算符
	 * @param c
	 * @return
	 */
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
	 * 根据后缀表达式得到根节点的firstpos函数结果集合和各结点的followpos函数结果集合
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

		int[][] tree = createTree(postfix); // 根据后缀表达式创建语法树

		for (int i = 0; i < length; i++) {  // 初始化集合数组
			for (int j = 0; j < length; j++) {
				firstpos[i][j] = lastpos[i][j] = followpos[i][j] = 0;
			}
		}
		for (int i = 0; i < length; i++) { // 求出各节点的nullable(n)、firstpos(n)、lastpos(n)、followpos(n)。nullable(n)
			if (Isalpha(postfix.charAt(i)) || postfix.charAt(i) == '#') {  // 字母或终结符（语法树的叶子结点）
				position[i] = pos;  // 为字母或终结符编号
				pos++;
				nullable[i] = false;
				firstpos[i][0] = position[i];
				lastpos[i][0] = position[i];
			} else {  // 运算符（语法树的双亲结点）
				position[i] = 0;// 运算符的position设为0
				if (postfix.charAt(i) == '|') {
					// nullable(i) = nullable(c1) or nullable(c2)
					nullable[i] = (nullable[tree[i][0]] || nullable[tree[i][1]]);

					// firstpos(i) = firstpos(c1) U firstpos(c2)
					// lastpos(i) = lastpos (c1) U lastpos (c2)
					int j = 0;
					while (firstpos[tree[i][1]][j] != 0) {
						firstpos[i][j] = firstpos[tree[i][1]][j];
						lastpos[i][j] = lastpos[tree[i][1]][j];
						j++;
					}
					int k = 0;
					int m;
					while (firstpos[tree[i][0]][k] != 0) {
						for (m = 0; m < j; m++) {
							if (firstpos[i][m] == firstpos[tree[i][0]][k])
								break;
						}
						if (m == j) {
							firstpos[i][j] = firstpos[tree[i][0]][k];
							lastpos[i][j] = lastpos[tree[i][0]][k];
							j++;
						}
						k++;
					}

				} else if (postfix.charAt(i) == '*') {
					nullable[i] = true;

					// firstpos(i) = firstpos(c1)
					// lastpos(i) = lastpos (c1)
					int j = 0;
					while (firstpos[tree[i][0]][j] != 0) {
						firstpos[i][j] = firstpos[tree[i][0]][j];
						lastpos[i][j] = lastpos[tree[i][0]][j];
						j++;
					}
				} else { // 连接符
					nullable[i] = (nullable[tree[i][0]] && nullable[tree[i][1]]);

					if (nullable[tree[i][0]] == true) { // firstpos(i) = firstpos(c1) U firstpos(c2)
						int j = 0;
						int m;
						while (firstpos[tree[i][1]][j] != 0) {
							firstpos[i][j] = firstpos[tree[i][1]][j];
							j++;
						}
						int k = 0;
						while (firstpos[tree[i][0]][k] != 0) {
							for (m = 0; m < j; m++) {
								if (firstpos[i][m] == firstpos[tree[i][0]][k])
									break;
							}
							if (m == j) {
								firstpos[i][j] = firstpos[tree[i][0]][k];
								j++;
							}
							k++;
						}
					} else { // firstpos(i) = firstpos(c1)
						int j = 0;
						while (firstpos[tree[i][0]][j] != 0) {
							firstpos[i][j] = firstpos[tree[i][0]][j];
							j++;
						}
					}

					if (nullable[tree[i][1]]) { // lastpos(i) = lastpos (c1) U lastpos (c2)
						int j = 0;
						while (lastpos[tree[i][1]][j] != 0) {
							lastpos[i][j] = lastpos[tree[i][1]][j];
							j++;
						}
						int k = 0;
						int m;
						while (lastpos[tree[i][0]][k] != 0) {
							for (m = 0; m < j; m++) {
								if (lastpos[i][m] == lastpos[tree[i][0]][k])
									break;
							}
							if (m == j) {
								lastpos[i][j] = lastpos[tree[i][0]][k];
								j++;
							}
							k++;
						}
					} else { // lastpos(i) = lastpos (c2)
						int j = 0;
						while (lastpos[tree[i][1]][j] != 0) {
							lastpos[i][j] = lastpos[tree[i][1]][j];
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

		for (int i = 0; i < length; i++) { // 求出各节点的followpos(n)
			if (postfix.charAt(i) == '&') {
				int m = 0;
				while (lastpos[tree[i][0]][m] != 0) {
					int k;
					for (k = 0; k < length; k++) {  // 寻找空余位置，以插入firstpos(c2)的结果集元素
						if (followpos[lastpos[tree[i][0]][m]][k] == 0)
							break;
					}
					int n = 0;
					while (firstpos[tree[i][1]][n] != 0) { // 对于lastpos(c1)中的所有位置i'，followpos(i') = firstpos(c2)
						followpos[lastpos[tree[i][0]][m]][k] = firstpos[tree[i][1]][n];
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
					for (n = 0; n < length; n++) {  // 寻找空余位置，以插入firstpos(n)的结果集元素
						if (followpos[lastpos[i][m]][n] == 0)
							break;
					}
					int k = 0;
					while (firstpos[i][k] != 0) { // 对于lastpos(n)中的所有位置i'，followpos(i') = firstpos(n)
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

	/**
	 * 创建后缀表达式的语法树
	 * 
	 * @param postfix
	 * @return
	 */
	public int[][] createTree(String postfix) throws EmptyStackException {
		Stack<Integer> st = new Stack<Integer>();
		int[][] tree = new int[postfix.length()][2];
		for (int i = 0; i < postfix.length(); i++) { // 初始化语法树
			for (int j = 0; j < 2; j++) {
				tree[i][j] = -1;
			}
		}
		for (int i = 0; i < postfix.length(); i++) {
			switch (postfix.charAt(i)) {
			case '*':
				tree[i][0] = st.pop();
				st.push(i);
				break;
			case '&':
				tree[i][1] = st.pop();
				tree[i][0] = st.pop();
				st.push(i);
				break;
			case '|':
				tree[i][1] = st.pop();
				tree[i][0] = st.pop();
				st.push(i);
				break;
			default:
				if (Isalpha(postfix.charAt(i)) || postfix.charAt(i) == '#') {
					st.push(i);
				}
			}
		}
		return tree;
	}

	/**
	 * 检查字符是否为字母
	 * @param c
	 * @return
	 */
	private boolean Isalpha(char c) {
		if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
			return true;
		else
			return false;
	}

	/**
	 * 构造状态矩阵
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public StateMatrix createStateMatrix(Map<String, Object> map) {
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
		stateStack.push((Set<Integer>) map.get("firstpos")); // 将firstpos(root)的结果集作为初始状态入栈
		while (!stateStack.isEmpty()) {
			Set<Integer> state = stateStack.pop();
			stateList.add(state); // 将栈顶状态state出栈并加入状态集中
			for (Character ch : set) {
				Set<Integer> tmpSet = new HashSet<Integer>();
				for (int i : state) { // 求state中和ch对应的所有位置n的followpos(n)的并集tmpSet
					if (labelMap.get(i) == ch) {
						tmpSet.addAll(followposMap.get(i));
					}
				}
				if (!tmpSet.isEmpty()) {
					int eq = 0;
					for (Set<Integer> set1 : stateList) {
						if (isSetEqual(set1, tmpSet)) {
							eq = 1;
							break;
						}
					}
					if (eq == 0) { // tmpSet不在状态集中，则入栈
						stateStack.push(tmpSet);
					}
					List<Object> rel = new ArrayList<Object>();
					rel.add(state);
					rel.add(ch);
					rel.add(tmpSet);
					tran.add(rel); // 在状态转换集中添加转换关系(state，ch)->tmpSet
				}
			}
		}
		List<Character> labelList = new ArrayList<Character>();
		labelList.addAll(set);
		int[][] intMatrix = new int[stateList.size()][set.size() + 1];
		for (int i = 0; i < stateList.size(); i++) { // 初始化二维整型数组
			for (int j = 0; j < set.size() + 1; j++) {
				intMatrix[i][j] = -1;
			}
		}
		Set<Integer> state = new HashSet<Integer>();
		int i = -1, j = 0;
		for (List<Object> rela : tran) {  // 将状态转换集中的转换关系处理后添加到二维整型数组中
			if (!isSetEqual(state, (Set<Integer>) rela.get(0))) {
				j = 0;
				i++;
				intMatrix[i][j] = stateList.indexOf(rela.get(0));
			}
			j = 1 + labelList.indexOf(rela.get(1));
			intMatrix[i][j] = stateList.indexOf(rela.get(2));
			state = (Set<Integer>) rela.get(0);
		}
		
		StateMatrix stateMatrix = new StateMatrix();
		stateMatrix.setMatrix(intMatrix);
		char[] labelArray = new char[labelList.size() + 1];
		labelArray[0] = ' ';
		int i2 = 1;
		for (char ch : labelList) { // 将字符集存放到字符数组中
			labelArray[i2] = ch;
			i2++;
		}
		stateMatrix.setInCh(labelArray); // 添加字符集到stateMatrix对象中
		return stateMatrix;
	}

	/**
	 * 判断两个集合是否相等
	 * @param set1
	 * @param set2
	 * @return
	 */
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
