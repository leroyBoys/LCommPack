/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.comm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class BoxRandomTool {
	public static int  k = 0;
	public static void main(String[] args){
		BoxRandomTool.test();
	}
	 public static void test(){
		 long time = System.currentTimeMillis();
		 List<Map<String,Double>> list = new  ArrayList<Map<String,Double> >();
		 list.add(BoxRandomTool.get(10.0));
		 list.add(BoxRandomTool.get(11.0));
		 list.add(BoxRandomTool.get(11.55));
		 list.add(BoxRandomTool.get(11.45));
		 list.add(BoxRandomTool.get(6.0));
		 list.add(BoxRandomTool.get(40.5));
		 list.add(BoxRandomTool.get(9.5));
		 list.add(BoxRandomTool.get(100.0));
		 list.add(BoxRandomTool.get(100.0));
		 list.add(BoxRandomTool.get(100.0));
		 System.out.println( System.currentTimeMillis() - time+"--");
		 time = System.currentTimeMillis();
		 BoxRandomTool.openBox(list);
		 System.out.println( System.currentTimeMillis() - time+"over");
	}

	public static Map<String,Double>  get(double i){
		Map<String,Double>  map = new HashMap<String, Double>();
		map.put("rate", i);
		map.put("k", Double.valueOf(k));
		k++;
		return map;
	}

	public static void  openBox(List<Map<String,Double>> list){
		int maxLength = 1;
		List<Map<String,Double>> getList = new ArrayList<Map<String,Double>>();
		List<Map<String,Double>> radnomList = new ArrayList<Map<String,Double>>();
		Double cur = 0d;
		for(Map<String,Double> map:list){
			Double d = map.get("rate");
			if(d == 100){
				System.out.println(d);
				getList.add(map);
				continue;
			}
			cur += d;
			int l = d.toString().length();
			if(l > maxLength){
				maxLength = l;
			}
			map.put("rate", cur);
			radnomList.add(map);
		}
		maxLength = get(maxLength-1);
		int random = new Random().nextInt(maxLength*100)+1;
		System.out.println("随机数："+random);
		for(Map<String,Double> map:radnomList){
			Double d = map.get("rate");
			double now = d*maxLength;
			if(random <=  now){
				System.out.println("抽奖概率得到："+d+"->"+map.get("k"));
				continue;
			}
			System.out.println(d+"->"+map.get("k"));
		}
		for(Map<String,Double> map:getList){
			Double d = map.get("rate");
			System.out.println("100%抽奖概率得到："+d+"->"+map.get("k"));
		}
		
	}
	public static int  get(int j){
		int max = 1;
		for(int i = 0;i<j;i++){
			max *= 10;
		}
		return max;
	}
}
