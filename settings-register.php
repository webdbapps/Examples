<?php
/**
 * Created by PhpStorm.
 * User: schilders
 * Date: 11/14/2017
 * Time: 3:38 PM
 * QFPPlugin - Register Settings
 */
if ( ! defined( 'ABSPATH' ) ){
	exit;
}
function qfpplugin_register_settings(){
	register_setting(
		'qfpplugin_options',
		'qfpplugin_options',
		'qfpplugin_validate_callback_options'
	);
	add_settings_section(
		'qfpplugin_section_login',
		'Customize Login Page',
		'qfpplugin_callback_section_login',
		'qfpplugin'
	);
	add_settings_section(
		'qfpplugin_section_admin',
		'Customize Admin Area',
		'qfpplugin_callback_section_admin',
		'qfpplugin'
	);
	add_settings_field(
		'custom_url',
		'Custom URL',
		'qfpplugin_callback_field_text',
		'qfpplugin',
		'qfpplugin_section_login',
		['id' => 'custom_url', 'label' => 'Custom URL for the Login link.']
	);
	add_settings_field(
		'custom_title',
		'Custom Title',
		'qfpplugin_callback_field_text',
		'qfpplugin',
		'qfpplugin_section_input',
		[ 'id' => 'custom_title', 'label' => 'Custom title attribute' ]
	);
	add_settings_field(
		'custom_style',
		'Custom Style',
		'qfpplugin_callback_field_radio',
		'qfpplugin',
		'qfpplugin_section_login',
		['id' => 'custom_style', 'label' => 'Custom CSS for the login' ]
	);
	add_settings_field(
		'custom_message',
		'Custom Message',
		'qfpplugin_callback_field_textarea',
		'qfpplugin',
		'qfpplugin_section_input',
		['id' => 'custom_message', 'label' => 'Custom text and/or markup' ]
	);
	add_settings_field(
		'custom_footer',
		'Custom Footer',
		'qfpplugin_callback_field_text',
		'qfpplugin',
		'qfpplugin_section_admin',
		['id' => 'custom_footer', 'label' => 'Custom Footer text' ]
	);
	add_settings_field(
		'custom_toolbar',
		'Custom Toolbar',
		'qfpplugin_callback_field_checkbox',
		'qfpplugin',
		'qfpplugin_section_admin',
		['id' => 'custom_toolbar', 'label' => 'Remove new post and comment from toolbar' ]
	);
	add_settings_field(
		'custom_scheme',
		'Custom Scheme',
		'qfpplugin_callback_field_select',
		'qfpplugin',
		'qfpplugin_section_admin',
		['id' => 'custom_scheme', 'label' => 'Default color scheme' ]
	);
}
add_action( 'admin_init', 'qfpplugin_register_settings' );
