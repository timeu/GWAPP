package com.gmi.gwaswebapp.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;

public interface MyResources extends ClientBundle {
	
	 public interface MainStyle extends CssResource {
		 String searchbox_container();
		 String searchbox();
		 String searchbox_white();
		 String spacer();
		 String infoBox();
		 String formbox();
		 String searchbox_apple();
		 String searchbox_dark();
		 String small();
		 String clearfix();
		 String box_shadow();
		 String box_container();
		 String pager_container();
		 String filterbox_container();
		 String filterbox();
		 String filterbox_header_row();
		 String filterbox_grey_row();
		 String filterbox_search_criterias();
		 String title();
		 String cellTable();
		 String round_button();
		 String round_button_selected();
		 String tree_container();
		 String tree_header();
		 String content_container();
		 String help_container();
		 String help_section();
		 String help_image();
		 String help_image_container();
		 String help_image_close_link();
		 String header();
		 String transplant_logo();
		 String fp7_logo();
		 String eu_flag();
	 }
	 
	 @Source("logo.png")
	 ImageResource logo();
	 
	 @Source("FP7_logo_medium.png")
	 ImageResource fp7_logo();

	 @Source("transplant_logo_medium.png")
	 ImageResource transplant_logo();
	 
	 @Source("flag_eu_medium.png")
	 ImageResource eu_flag();
	 
	 @Source("search_white.png")
	 ImageResource search_white();
	 
	 @Source("search_dark.png")
	 ImageResource search_black();
	 
	 @Source("screens/upload_phenotypes.png")
	 DataResource upload_phenotypes();
	 
	 @Source("screens/phenotype_list.png")
	 DataResource phenotype_list();
	 
	 @Source("screens/create_subsets.png")
	 DataResource create_subsets();
	 
	 @Source("screens/apply_transformations.png")
	 DataResource apply_transformations();
	 
	 @Source("screens/run_gwas.png")
	 DataResource run_gwas();
	 
	 @ImageOptions(preventInlining=true)
	 @Source("close.png")
	 ImageResource close_button();
	 
	 @Source("screens/view_results.png")
	 DataResource view_results();
	 
	 @Source("screens/run_step_gwas.png")
	 DataResource run_step_gwas();
	 
	 @Source("screens/accessions.png")
	 DataResource accessions();
	 
	 @Source("style.css")
	 MainStyle style();
}
