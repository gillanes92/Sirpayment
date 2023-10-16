(function() {
    if (!this.VisanetCheckout) {
        this.VisanetCheckout = {};
        var e, t, n, i, o, r, s = document.currentScript || (e = document.getElementsByTagName("script"))[e.length - 1];
        this.VisanetCheckout.initfp = !1, this.VisanetCheckout.isNumeric = function(e, t) {
            "." === e.toString().substring(e.length - 1) && (e = e.toString() + "0");
            var n = new RegExp("^\\s*-?(\\d+(\\.\\d{1," + t + "})?|\\.\\d{1," + t + "})\\s*$", "g");
            return -1 === t && (n = new RegExp("^\\s*-?(\\d+(\\.\\d{1,25})?|\\.\\d{1,25})\\s*$", "g")), n.test(e)
        }, this.VisanetCheckout.isSpanish = (t = (navigator.language || navigator.userLanguage).split("-"), n = !1, t.length >= 1 && (n = "es" === t[0]), n), this.VisanetCheckout.onCI = -1 !== s.src.indexOf("qa=true"), this.VisanetCheckout.onProd = -1 !== (i = s.src, r = document.createElement("div"), r.innerHTML = "<a></a>", r.firstChild.href = i, r.innerHTML = r.innerHTML, (o = r.firstChild).protocol + "//" + o.host).indexOf("static-content.vnforapps.com"), this.VisanetCheckout.host = this.VisanetCheckout.onProd ? "https://static-content.vnforapps.com/v1" : "https://static-content-qas.vnforapps.com/vMigrated", this.VisanetCheckout.scriptTag = s
    }
}).call(this),
    function(e) {
        this.VisanetCheckout.validate || (this.VisanetCheckout.validate = {
            inValues: function() {
                for (var e = 1; e < arguments.length; e++)
                    if (arguments[e] == arguments[0]) return !0;
                return !1
            },
            preprocess: function(e) {
                var t, n = !1,
                    i = !1;
                if (null == e) return null;
                if (null === e.action || void 0 === e.action) return null;
                if (!!e.recurrencetype && this.inValues(e.recurrencetype.toLowerCase(), "fixed", "fixedinitial") || (e.recurrenceamount = e.recurrenceamount ? e.recurrenceamount : "0.00"), e.currency = null === e.currency || void 0 === e.currency ? "PEN" : "USD" !== e.currency ? "PEN" : e.currency, t = void 0 !== e.recurrence && null !== e.recurrence && "true" === e.recurrence.toString().trim().toLowerCase(), n = void 0 !== e.amount && null !== e.amount ? VisanetCheckout.isNumeric(e.amount, 2) : n, i = e.recurrenceamount ? VisanetCheckout.isNumeric(e.recurrenceamount, 2) : i, t) {
                    if (!i) return alert("Field recurrenceamount has to be set as a numeric value when recurrence has been set to true."), null;
                    !n && i ? (e.amount = e.recurrenceamount, n = !0) : n && !i && (e.recurrenceamount = e.amount, i = !0), e.recurrencetype && (e.recurrencetype = this.inValues(e.recurrencetype.toLowerCase(), "fixed", "fixedinitial", "variable", "variableinitial") ? e.recurrencetype.toLowerCase() : "fixed", e.recurrenceamount = this.inValues(e.recurrencetype.toLowerCase(), "variableinitial", "variable") ? 0 : e.recurrenceamount), e.recurrencefrequency && (e.recurrencefrequency = this.inValues(e.recurrencefrequency.toLowerCase(), "annual", "monthly", "biannual", "quarterly") ? e.recurrencefrequency.toLowerCase() : "monthly")
                } else if (!n) return null;
                return e.isrecurrence = t, e
            },
            combinate: function(e, t) {
                return e && t && (t.amount = e.amount && VisanetCheckout.isNumeric(e.amount, 2) ? e.amount : t.amount, t.currency = e.currency ? e.currency : t.currency, t.merchantlogo = e.merchantlogo ? e.merchantlogo : t.merchantlogo, t.merchantid = e.merchantid ? e.merchantid : t.merchantid, t.merchantname = e.merchantname ? e.merchantname : t.merchantname, t.formbuttoncolor = e.formbuttoncolor ? e.formbuttoncolor : t.formbuttoncolor, t.showamount = e.showamount ? e.showamount : t.showamount, t.purchasenumber = e.purchasenumber ? e.purchasenumber : t.purchasenumber, t.cardholdername = e.cardholdername ? e.cardholdername : t.cardholdername, t.cardholderlastname = e.cardholderlastname ? e.cardholderlastname : t.cardholderlastname, t.cardholderemail = e.cardholderemail ? e.cardholderemail : t.cardholderemail, t.usertoken = e.usertoken ? e.usertoken : t.usertoken, t.recurrence = e.recurrence ? e.recurrence.toLowerCase().trim() : t.recurrence, t.frequency = e.frequency ? e.frequency : t.frequency, t.recurrencetype = e.recurrencetype ? e.recurrencetype : t.recurrencetype, t.recurrenceamount = e.recurrenceamount && VisanetCheckout.isNumeric(e.recurrenceamount, 2) ? e.recurrenceamount : t.recurrenceamount), t
            },
            isValid: function(e) {
            	console.log("2: "+e.merchantid);
                return !!e && (!!(e.merchantid && e.amount && e.currency && e.sessiontoken && e.purchasenumber) || (alert("At a minimum, the action, merchantid, sessiontoken, amount and purchasenumber options have to be set"), !1))
            }
        })
    }.call(this, document),
    function(e) {
        this.VisanetCheckout.utils || (this.VisanetCheckout.utils = {
            bind: function(e, t, n) {
                return e.addEventListener ? e.addEventListener(t, n, !1) : e.attachEvent ? e.attachEvent("on" + t, n) : void 0
            },
            bindFn: function(e, t) {
                return function() {
                    return e.apply(t, arguments)
                }
            },
            addJsLink: function(t) {
                var n = e.createElement("script");
                n.src = t, n.type = "text/javascript", e.getElementsByTagName("head")[0].appendChild(n)
            },
            addCssLink: function(t) {
                var n = e.createElement("link");
                n.type = "text/css", n.rel = "stylesheet", n.href = t, e.getElementsByTagName("head")[0].appendChild(n)
            },
            addInput: function(t, n) {
                var i = e.createElement("input");
                return i.type = "hidden", i.value = n, i.name = t, i
            }
        })
    }.call(this, document),
    function() {
        this.VisanetCheckout.ua || (this.VisanetCheckout.ua = {
            userAgent: window.navigator.userAgent,
            isiPhone: function() {
                return /(iPhone|iPod)/i.test(this.userAgent)
            },
            isiOSWebView: function() {
                return /(iPhone|iPod).*AppleWebKit(?!.*Safari)/i.test(this.userAgent)
            },
            isMobileDevice: function() {
                return this.isiPhone() && !this.isiOSWebView()
            }
        })
    }.call(this),
    function() {
        this.VisanetCheckout.Navigator || (this.VisanetCheckout.Navigator = function() {
            function e() {}
            return e.isSupportTLS1_2 = function() {
                var e = !0,
                    t = function() {
                        var e, t = navigator.userAgent,
                            n = t.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];
                        if (/trident/i.test(n[1])) return {
                            name: "IE",
                            version: (e = /\brv[ :]+(\d+)/g.exec(t) || [])[1] || ""
                        };
                        if ("Chrome" === n[1] && null != (e = t.match(/\bOPR\/(\d+)/))) return {
                            name: "Opera",
                            version: e[1]
                        };
                        n = n[2] ? [n[1], n[2]] : [navigator.appName, navigator.appVersion, "-?"], null != (e = t.match(/version\/(\d+)/i)) && n.splice(1, 1, e[1]);
                        return {
                            name: n[0],
                            version: n[1]
                        }
                    }(),
                    n = parseInt(t.version);
                switch (t.name.toLowerCase()) {
                    case "chrome":
                        n < 30 && (e = !1);
                        break;
                    case "opera":
                        n < 17 && (e = !1);
                        break;
                    case "ie":
                    case "msie":
                        n < 10 && (e = !1);
                        break;
                    case "safari":
                        n < 7 && (e = !1);
                        break;
                    case "firefox":
                        n < 27 && (e = !1)
                }
                return e
            }, e
        }())
    }.call(this),
    function() {
        if (!this.VisanetCheckout.RPC) {
            var e = this.VisanetCheckout.utils;
            this.VisanetCheckout.RPC = function() {
                function t(t) {
                    var n;
                    this.target = t.target, this.type = t.type || "server", this.methods = {}, this.messages = [], this.isReady = !1, e.bind(window, "message", (n = this, function() {
                        return n.handleMessage.apply(n, arguments)
                    }))
                }
                return t.prototype.sendMessage = function(e) {
                    data = JSON.stringify(e), this.send(data)
                }, t.prototype.send = function(e) {
                    !0 === this.isReady ? this.target.postMessage(e, "*") : this.messages.push(e)
                }, t.prototype.handleMessage = function(e) {
                    e.source == this.target && this.processMessage(e.data)
                }, t.prototype.processMessage = function(e) {
                    message = JSON.parse(e), !0 !== message.isReady || !1 !== this.isReady ? this.methods[message.method] && (this.methods[message.method], 1) && this.methods[message.method].apply(this.methods, message.args) : this.ready()
                }, t.prototype.whenReady = function(e) {
                    if (this.isReady) return e();
                    this.onReadyCallback = e
                }, t.prototype.ready = function() {
                    var e;
                    for (this.isReady = !0, this.onReadyCallback && this.onReadyCallback(), this.sendMessage({
                            isReady: !0,
                            type: this.type
                        }); e = this.messages.shift();) this.send(e)
                }, t.prototype.invoke = function() {
                    var e, t, n = Array.prototype.slice;
                    e = arguments[0], t = n.call(arguments, 1), this.sendMessage({
                        method: e,
                        args: t
                    })
                }, t
            }()
        }
    }.call(this),
    function(e) {
        if (!this.VisanetCheckout.Iframe) {
            var t = this.VisanetCheckout.RPC,
                n = this.VisanetCheckout.utils,
                i = this.VisanetCheckout;
            this.VisanetCheckout.Iframe = function() {
                var o, r, s, a, c, u, h, l, d = e.body;

                function p() {
                    var e = i.host + "/visanet.html?" + Math.random();
                    i.onCI && (e += "&qa=true"), this.opened = !1, this.src = e, this.iframe = this.addIframe(), this.rpc = new t({
                        target: this.iframe.contentWindow,
                        type: "server"
                    }), this.rpc.methods.cancel = n.bindFn(this.cancel, this), this.rpc.methods.addfingerprint = n.bindFn(this.addfingerprint, this), this.rpc.methods.qrcomplete = n.bindFn(this.qrcomplete, this), this.rpc.methods.close = n.bindFn(this.close, this), this.rpc.methods.complete = n.bindFn(this.complete, this)
                }
                return p.prototype.config = function(e) {
                    this.configuration = e || (e = {}), this.rpc.invoke("config", this.configuration)
                }, p.prototype.open = function(t) {
                    this.opened || (this.opened = !0, function() {
                        if (o = e.getElementsByTagName("head")[0], r = e.querySelector("meta[name=viewport]"), s = "", d = e.body, a = e.documentElement, c = a.style.cssText || "", u = d.style.cssText || "", h = "position: relative; overflow: hidden; height: 100%", !r) {
                            var t = e.createElement("meta");
                            t.name = "viewport", t.content = "", o.appendChild(t), r = t
                        }
                        s = r.getAttribute("content"), l = d.scrollTop, r.setAttribute("content", "width=device-width, user-scalable=no"), d.style.cssText = h, a.style.cssText = h
                    }(), this.iframe.style.display = "block", console.log("iframe display block"), this.rpc.invoke("open", t))
                }, p.prototype.complete = function(e) {
                    this.configuration.complete(e)
                }, p.prototype.qrcomplete = function(e) {
                    try {
                        console.log(JSON.stringify(e)), window.location.replace(e.url)
                    } catch (e) {
                        console.log(e)
                    }
                }, p.prototype.addfingerprint = function(e) {
                    try {
                        i.initfp || (initDFP(e.sessiontoken, e.purchasenumber, e.clientip, e.merchantid), i.initfp = !0)
                    } catch (e) {}
                }, p.prototype.cancel = function() {
                    this.close(), this.configuration.cancel && this.configuration.cancel()
                }, p.prototype.close = function() {
                	javascript:history.back(), this.opened = !1, this.iframe.style.display = "none", e.body.className = e.body.className.replace("visanet-opened", ""), r.setAttribute("content", s), d.style.cssText = c, a.style.cssText = u, d.scrollTop = l
                }, p.prototype.addIframe = function() {
                    var t, i = e.createElement("div");
                    return i.id = "visaNetWrapper", (t = e.createElement("iframe")).id = "visaNetJS", t.setAttribute("frameBorder", "0"), t.setAttribute("allowtransparency", "true"), t.style.cssText = "z-index: 2147483646;\ndisplay: none;\nbackground: transparent;\nbackground: rgba(0,0,0,0.005);\nborder: 0px none transparent;\noverflow-x: hidden;\noverflow-y: auto;\nvisibility: hidden;\nmargin: 0;\npadding: 0;\n-webkit-tap-highlight-color: transparent;\n-webkit-touch-callout: none; position: fixed;\nleft: 0;\ntop: 0;\nwidth: 100%;\nheight: 100%;", t.src = this.src, i.appendChild(t), e.body.appendChild(i), n.bind(t, "load", function() {
                        t.style.visibility = "visible"
                    }), t
                }, p
            }()
        }
    }.call(this, document),
    function(e) {
        if (!this.VisanetCheckout.Tab) {
            var t = this.VisanetCheckout.RPC,
                n = this.VisanetCheckout.utils,
                i = this.VisanetCheckout;
            this.VisanetCheckout.Tab = function() {
                function e() {
                    var e = i.host + "/visanet.html?" + Math.random();
                    i.onCI && (e += "&ci=true"), this.opened = !1, this.src = e
                }
                return e.prototype.config = function(e) {
                    this.configuration = e || (e = {}), this.configuration.inTab = !0
                }, e.prototype.open = function(e) {
                    this.opened || (this.opened = !0, this.createTab(), this.rpc.invoke("config", this.configuration), this.rpc.invoke("open", e))
                }, e.prototype.createTab = function() {
                    var e = this;
                    this.tab = window.open(this.src, "btfljs"), this.rpc = new t({
                        target: this.tab,
                        type: "server"
                    }), this.rpc.methods.cancel = n.bindFn(this.cancel, this), this.rpc.methods.addfingerprint = n.bindFn(this.addfingerprint, this), this.rpc.methods.qrcomplete = n.bindFn(this.qrcomplete, this), this.rpc.methods.complete = n.bindFn(this.complete, this), this.rpc.whenReady(function() {
                        e.checkIfClosed()
                    })
                }, e.prototype.checkIfClosed = function() {
                    var e = this;
                    this.checkInterval && clearInterval(this.checkInterval), this.checkInterval = setInterval(function() {
                        e.tab.closed && e.close()
                    }, 1e3)
                }, e.prototype.complete = function(e) {
                    this.close(), this.configuration.complete(e)
                }, e.prototype.addfingerprint = function(e) {
                    try {
                        i.initfp || (initDFP(e.sessiontoken, e.purchasenumber, e.clientip, e.merchantid), i.initfp = !0)
                    } catch (e) {}
                }, e.prototype.qrcomplete = function(e) {
                    try {
                        console.log(JSON.stringify(e)), window.location.replace(e.url)
                    } catch (e) {
                        console.log(e)
                    }
                }, e.prototype.cancel = function() {
                    this.close(), this.configuration.cancel && this.configuration.cancel()
                }, e.prototype.close = function() {
                	clearInterval(this.checkInterval), this.opened = !1, this.tab.closed || this.tab.close()
                }, e
            }()
        }
    }.call(this, document),
    function(e) {
        if (!this.VisanetCheckout.View) {
            var t = this.VisanetCheckout.ua,
                n = this.VisanetCheckout;
            this.VisanetCheckout.getView = function(e) {
                return t.isMobileDevice() && e.tabOnMobile ? n.Tab : n.Iframe
            }
        }
    }.call(this, document),
    function() {
        var e = "Sorry, but you can't make payment using this browser as its version is considered unsecure. Please, use latest version of your browser or download and install latest version of free Firefox / Chrome.";
        this.VisanetCheckout.Checkout || (!1 === this.VisanetCheckout.Navigator.isSupportTLS1_2() && alert(e), this.VisanetCheckout.Checkout = function() {
            var t;

            function n() {}
            return !1 === VisanetCheckout.Navigator.isSupportTLS1_2() ? (n.config = function(e) {}, n.open = function(t) {
                alert(e)
            }, n) : (this.VisanetCheckout.utils.addJsLink(this.VisanetCheckout.onCI ? this.VisanetCheckout.host + "/js/dev_dfp.js" : this.VisanetCheckout.host + "/js/prd_dfp.js"), n.config = function(e) {
                var n = this,
                    i = this.validate,
                    o = this.utils;
                (e = i.preprocess(e)) && i.isValid(e) && (this.configuration = e, this.configuration.complete = function(e) {
                    var t = document.createElement("form");
                    t.appendChild(o.addInput("transactionToken", e.token)), t.appendChild(o.addInput("customerEmail", e.email)), t.appendChild(o.addInput("channel", e.channel)), t.method = "POST", t.action = n.configuration.action, document.body.appendChild(t), t.submit()
                }, void 0 === t && (viewClass = VisanetCheckout.getView(e), t = new viewClass), t.config(e))
            }, n.open = function(e) {
                var n = this.validate;
                this.configuration ? e ? (this.configuration = n.combinate(e, this.configuration), this.configuration = n.preprocess(this.configuration), this.configuration && n.isValid(this.configuration) && (t.config(this.configuration), t.open(this.configuration))) : t.open(this.configuration) : alert("At a minimum, the action, merchantid, sessiontoken, amount and purchasenumber options have to be set")
            }, n)
        }())
    }.call(this),
    function() {
        if (!this.VisanetCheckout.Button) {
            var e = this.VisanetCheckout.utils,
                t = this.VisanetCheckout,
                n = window.attachEvent && !window.addEventListener;
            t.Button = function() {
                function i(n, i) {
                    this.form = n, this.complete = e.bindFn(this.complete, this), this.open = e.bindFn(this.open, this), this.cancel = e.bindFn(this.cancel, this), this.config = i, this.config.complete = this.complete, this.config.cancel = this.cancel, this.config.tabOnMobile = !1, this.params = {
                        amount: i.amount,
                        currency: i.currency,
                        cardholderemail: i.cardholderemail,
                        cardholdername: i.cardholdername,
                        cardholderlastname: i.cardholderlastname,
                        sessiontoken: i.sessiontoken,
                        purchasenumber: i.purchasenumber,
                        merchantid:i.merchantid
                    }, t.configure(this.config), this.render()
                }
                return i.prototype.render = function() {
                    var n = t.host + "/img/button/",
                        i = !!this.config.recurrence && "true" === this.config.recurrence.toString().trim().toLowerCase();
                    e.addCssLink(t.host + "/css/button.css"), this.btn = document.createElement("button"), this.btn.setAttribute("type", "submit"), this.config.buttonsize = this.config.buttonsize ? this.config.buttonsize.trim().toLowerCase() : "default", this.config.buttoncolor = this.config.buttoncolor ? this.config.buttoncolor.trim().toLowerCase() : "navy", t.validate.inValues(this.config.buttonsize, "small", "medium", "large") || (this.config.buttonsize = "default"), t.validate.inValues(this.config.buttoncolor, "navy", "gray") || (this.config.buttoncolor = "navy"), this.btn.className = "start-js-btn modal-opener " + (this.config.buttonsize ? this.config.buttonsize : "default"), this.btn.style = "true" === this.config.custom ? "display:none;" : "", n += t.isSpanish ? "ES/" : "EN/", n += this.config.buttoncolor, n += "/" + this.config.buttonsize + (i ? "/SubscribeWith.png" : "/PayWith.png"), this.btn.style.background = 'url("' + n + '")', e.bind(this.btn, "click", this.open), this.form.appendChild(this.btn)
                }, i.prototype.open = function(e) {
                    return e.preventDefault ? e.preventDefault() : e.returnValue = !1, n ? alert("Due to security reasons please, use modern browser (IE11, FireFox, Chrome or Safari) to make payment!") : (navigator.userAgent.match(/iPhone|iPad|iPod/i) && (window.scrollTo(0, 0), document.body.className += "visanet-opened"), t.open(this.params)), !1
                }, i.prototype.complete = function(t) {
                    this.form.appendChild(e.addInput("transactionToken", t.token)), this.form.appendChild(e.addInput("customerEmail", t.email)), this.form.appendChild(e.addInput("channel", t.channel)), this.form.submit()
                }, i.prototype.cancel = function() {}, i
            }()
        }
    }.call(this),
    function() {
        this.VisanetCheckout.open || (this.VisanetCheckout.open = this.VisanetCheckout.Checkout.open, this.VisanetCheckout.configure = this.VisanetCheckout.Checkout.config)
    }.call(this),
    function() {
        if (!this.VisanetCheckout.auto) {
            this.VisanetCheckout.auto = !0;
            var e = this.VisanetCheckout.scriptTag,
                t = e.parentElement,
                n = {};
            if ("FORM" == t.tagName) {
                var o, r;
                for (i = 0; i < e.attributes.length; i++) null !== (r = (o = e.attributes[i]).name.match(/^data-(.+)$/)) && (paramName = a(r[1]), n[paramName] = o.value);
                var s = t.getAttribute("action");
                if (n.action = s, null != n) {
                    n.tabOnMobile = !1;
                    new this.VisanetCheckout.Button(t, n)
                }
            }
        }

        function a(e) {
            return e.replace(/(?:[-_])(\w)/g, function(e, t) {
                return t.toUpperCase()
            })
        }
    }.call(this);