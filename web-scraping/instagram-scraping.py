# instagram-scraping.py

# get_ipython().system('pip uninstall selenium -y')
# get_ipython().system('pip install selenium==3.141.0')
# get_ipython().system('pip install bs4')

import time
from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.webdriver.common.keys import Keys

import re, unicodedata

import pandas as pd 
import numpy as np


def insta_search(word):
    url = 'https://www.instagram.com/explore/tags/' + word
    return url 

def select_first(driver):
    first = driver.find_element_by_css_selector('div._9AhH0')
    first.click()
    time.sleep(5)

def get_content(driver, district):
    html = driver.page_source
    soup = BeautifulSoup(html, 'lxml')
    
    try:
        place = soup.select('a.O4GlU')[0].text
        place = unicodedata.normalize('NFC', place)
    except:
        place = ''
        
    date = soup.select('time.FH9sR.Nzb55')[0]['datetime'][:10]
        
    data = [district, place, date]
    
    return data 

def move_next(driver): # 다음 게시글 조회
    right = driver.find_element_by_css_selector('div.l8mY4.feth3 > button.wpO6b')
    right.send_keys(Keys.ENTER)
    time.sleep(3)

def web_scraping(email, password):
    # 검색 키워드
    theme_list = ['맛집', '카페', '술집', '핫플']
    district_list = ['마포', '종로', '용산', '강남', '송파', '영등포', '한남', '신촌', '홍대', '을지로', '여의도', '성수', '연남', '북촌', '압구정', '판교', '분당', '대학로', '건대', '잠실', '이태원']
    
    # 크롬 원격 접속 (인스타그램)
    url = 'https://www.instagram.com'
    driver = webdriver.Chrome(executable_path='./chromedriver')
    driver.maximize_window()
    driver.get(url)
    time.sleep(10) 

    # 인스타그램 로그인 (이메일)
    input_id = driver.find_elements_by_css_selector('input._2hvTZ.pexuQ.zyHYP')[0]
    input_id.clear()
    input_id.send_keys(email)

    # 인스타그램 로그인 (비밀번호)
    input_pw = driver.find_elements_by_css_selector('input._2hvTZ.pexuQ.zyHYP')[1]
    input_pw.clear()
    input_pw.send_keys(password)
    input_pw.submit()

    # NoSuchElementException 방지를 위한 시간 대기 메소드
    time.sleep(10)

    results = [ ] 

    for district in district_list:
        for theme in theme_list:
            url = insta_search(district+theme)
            driver.get(url)
            time.sleep(10)

            select_first(driver)

            target = 100
            for i in range(target):
                try:
                    data = get_content(driver, district)
                    results.append(data)
                    move_next(driver)   
                except:
                    time.sleep(5)
                    move_next(driver)
                # print(results[:])
                
    ## 데이터프레임 생성
    results_df = pd.DataFrame(results)
    results_df.columns = ['district', 'place', 'date']

    results_df['place'].replace('', np.nan, inplace=True)
    results_df.dropna(subset=['place'], inplace=True)

    results_df['count'] = results_df.groupby('place')['place'].transform('count')
    results_df = results_df.drop_duplicates(subset=['place'])

    js = results_df.to_json(orient='records', force_ascii=False)

    # 데이터프레임 CSV 파일로 변환
    results_csv = results_df.to_csv('./instagram_web_scraping.csv',index=False)
    
    return js;
