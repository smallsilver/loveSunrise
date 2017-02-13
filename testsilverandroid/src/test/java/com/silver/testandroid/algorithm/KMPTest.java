package com.silver.testandroid.algorithm;

import org.junit.Test;

import java.util.Arrays;

/**
 * @PACKAGE com.silver.testandroid.algorithm
 * @DESCRIPTION: TODO
 * @AUTHOR dongen_wang
 * @DATE 2/13/17 11:43
 * @VERSION V1.0
 */
public class KMPTest {

    @Test
    public void testKmp(){
        String searchStr= "ABCAACBBCBADAABCACBD";
        int[] next = new int[searchStr.length()];
        makeNext(searchStr.toCharArray(),next);
        System.out.println(Arrays.toString(next));
        int[] gNext = new int[searchStr.length()];
        GetNext(searchStr.toCharArray(),gNext);
        System.out.println(Arrays.toString(gNext));


        String search = "AAB";
        int index = KMPAlgorithm(searchStr,search);
        System.out.println("index---> "+index);
    }

    int KMPAlgorithm(String parent,String search){
        int i = 0,j = 0,plen = parent.length(),sLen = search.length();
        int[] next = new int[parent.length()];
        GetNext(parent.toCharArray(),next);
        while(i < plen && j < sLen ){
            if( j == -1 ||  parent.charAt(i) == search.charAt(j)){
                i++;
                j++;
            }else{
                j = next[j];
            }
        }
        if(j == sLen)
            return i - j;
        return -1;
    }


    void makeNext( char P[],int next[])
    {
        int q,k = 0;//q:模版字符串下标；k:最大前后缀长度
        int pLen = P.length;//模版字符串长度
        next[0] = 0;//模版字符串的第一个字符的最大前后缀长度为0
        for (q = 1; q < pLen; q++)//for循环，从第二个字符开始，依次计算每一个字符对应的next值
        {
            while(k > 0 && P[q] != P[k])//递归的求出P[0]···P[q]的最大的相同的前后缀长度k
                k = next[k-1];          //不理解没关系看下面的分析，这个while循环是整段代码的精髓所在，确实不好理解
            if (P[q] == P[k])//如果相等，那么最大相同前后缀长度加1
            {
                k++;
            }
            next[q] = k;
        }
    }

    private void GetNext(char[] p,int next[])
    {
        int pLen = p.length;
        next[0] = -1;
        int k = -1;
        int j = 0;
        while (j < pLen - 1)
        {
            //p[k]表示前缀，p[j]表示后缀
            if (k == -1 || p[j] == p[k])
            {
                ++k;
                ++j;
                next[j] = k;
            }
            else
            {
                k = next[k];
            }
        }
    }

}
