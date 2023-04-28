/* 
* Licensed to dotCMS LLC under the dotCMS Enterprise License (the
* “Enterprise License”) found below 
* 
* Copyright (c) 2023 dotCMS Inc.
* 
* With regard to the dotCMS Software and this code:
* 
* This software, source code and associated documentation files (the
* "Software")  may only be modified and used if you (and any entity that
* you represent) have:
* 
* 1. Agreed to and are in compliance with, the dotCMS Subscription Terms
* of Service, available at https://www.dotcms.com/terms (the “Enterprise
* Terms”) or have another agreement governing the licensing and use of the
* Software between you and dotCMS. 2. Each dotCMS instance that uses
* enterprise features enabled by the code in this directory is licensed
* under these agreements and has a separate and valid dotCMS Enterprise
* server key issued by dotCMS.
* 
* Subject to these terms, you are free to modify this Software and publish
* patches to the Software if you agree that dotCMS and/or its licensors
* (as applicable) retain all right, title and interest in and to all such
* modifications and/or patches, and all such modifications and/or patches
* may only be used, copied, modified, displayed, distributed, or otherwise
* exploited with a valid dotCMS Enterprise license for the correct number
* of dotCMS instances.  You agree that dotCMS and/or its licensors (as
* applicable) retain all right, title and interest in and to all such
* modifications.  You are not granted any other rights beyond what is
* expressly stated herein.  Subject to the foregoing, it is forbidden to
* copy, merge, publish, distribute, sublicense, and/or sell the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
* OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
* IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
* CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
* TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
* SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
* 
* For all third party components incorporated into the dotCMS Software,
* those components are licensed under the original license provided by the
* owner of the applicable component.
*/

package com.dotcms.enterprise.achecker.tinymce;

import java.util.List;
import java.util.Map;

import com.dotcms.enterprise.LicenseUtil;
import com.dotcms.enterprise.achecker.ACheckerRequest;
import com.dotcms.enterprise.achecker.ACheckerResponse;
import com.dotcms.enterprise.achecker.dao.GuidelinesDAO;
import com.dotcms.enterprise.achecker.dao.LangCodesDAO;
import com.dotcms.enterprise.achecker.impl.ACheckerImpl;
import com.dotcms.enterprise.achecker.model.GuideLineBean;
import com.dotcms.enterprise.achecker.tinymce.DaoLocator;
import com.dotcms.enterprise.license.LicenseLevel;


public class ACheckerDWR {

	private List<GuideLineBean> listaGuidelines = null;

	private List<GuideLineBean> getListaGudelines() throws Exception {
	    if(LicenseUtil.getLevel()< LicenseLevel.STANDARD.level)
	        throw new RuntimeException("need enterprise license");
		try{
			if( listaGuidelines == null ){
				GuidelinesDAO gLines = DaoLocator.getGuidelinesDAO();
				listaGuidelines = gLines.getOpenGuidelines();		 
			}
			return listaGuidelines;
		}catch (Exception e) {
			throw e; 
		}
	}


	public List<GuideLineBean> getSupportedGudelines(){	 	
	    if(LicenseUtil.getLevel()<LicenseLevel.STANDARD.level)
            throw new RuntimeException("need enterprise license");
		try{
			 return  getListaGudelines();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}

 
	public ACheckerResponse validate(Map<String, String> params) {
	    if(LicenseUtil.getLevel()<LicenseLevel.STANDARD.level)
            throw new RuntimeException("need enterprise license");
		try {
			String lang = params.get("lang");
			String content = params.get("content");
			String guidelines = params.get("guidelines");
			String fragment = params.get("fragment");
			if( lang != null && lang.trim().length() == 2  ){
				LangCodesDAO langCodeDao = DaoLocator.getLangCodesDAO();  
				langCodeDao .getLangCodeBy3LetterCode( lang  );
			}
	 		String toValidate =  content ;
			ACheckerRequest request = new ACheckerRequest(lang, toValidate, guidelines, Boolean.parseBoolean(fragment) );	
			ACheckerImpl achecker = new ACheckerImpl();
			return achecker.validate(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	
	

}