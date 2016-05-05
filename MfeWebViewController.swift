//
//  mfeWebViewController.swift
//  MobileFirstEnt
//
//  Created by Anastacio Gianareas on 6/21/15.
//  Copyright (c) 2015 mfe. All rights reserved.
//

import UIKit

class MfeWebViewController: UIViewController {

	@IBOutlet weak var webView: UIWebView!
	
	
    override func viewDidLoad() {
        super.viewDidLoad()
		
		var config: Config = Config()
		
		let url = NSURL(string: (config.getServiceDictornary()["WebPortal"] as! String) )
		let request = NSURLRequest(URL: url!)
		webView.loadRequest(request)
		
        // Do any additional setup after loading the view.
    }

	@IBAction func close(sender: AnyObject) {
		
		self.dismissViewControllerAnimated(true, completion: nil)
	}
   
}
