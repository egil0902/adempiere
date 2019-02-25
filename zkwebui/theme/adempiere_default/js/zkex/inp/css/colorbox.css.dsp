<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %><%@ taglib uri="http://www.zkoss.org/dsp/zk/core" prefix="z" %><%@ taglib uri="http://www.zkoss.org/dsp/web/theme" prefix="t" %>.z-colorbox{display:inline-block;width:40px;height:24px;border:1px solid #9c9c9c;${t:borderRadius('3px') };margin:0 2px;padding:3px;background:#fff;vertical-align:middle;position:relative;overflow:hidden;cursor:pointer}.z-colorbox-current{display:inline-block;width:100%;height:100%;position:relative}.z-colorbox-button{font-size:11px;display:inline-block;width:13px;height:7px;line-height:normal;background:#fff;position:absolute;bottom:1px;right:0;overflow:hidden}.z-colorbox-icon{font-size:14px;color:#636363;position:relative;top:-3px;left:3px}.z-colorbox-disabled,.z-colorbox-disabled *{color:#aaa!important;opacity:.6;filter:alpha(opacity=60);cursor:default!important}.z-colorbox-popup,.z-menu-popup{display:none;position:absolute;overflow:auto;z-index:88000;${t:boxShadow('1px 1px 3px rgba(0, 0, 0, 0.5)') }}.z-colorpicker{width:384px;height:332px;border:1px solid #9c9c9c;background:#fff;position:relative;overflow:hidden}.z-colorpicker-gradient{width:256px;height:256px;border:1px solid #9c9c9c;position:absolute;left:7px;top:31px;cursor:crosshair}.z-colorpicker-overlay{width:256px;height:256px;background-image:url(${c:encodeURL('~./zkex/img/colorbox/colorpicker_gradient.png')})}.z-colorpicker-bar{width:12px;height:256px;border:1px solid #9c9c9c;background-image:url(${c:encodeURL('~./zkex/img/colorbox/colorpicker_hue.png')});position:absolute;left:7px;overflow:hidden;cursor:n-resize}.z-colorpicker-circle{width:11px;height:11px;margin:-5px 0 0 -5px;background-image:url(${c:encodeURL('~./zkex/img/colorbox/colorpicker_select.gif')});position:absolute;top:0;left:0;overflow:hidden}.z-colorpicker-hue{width:27px;height:256px;position:absolute;top:31px;left:272px}.z-colorpicker-arrows{width:27px;height:9px;margin:-4px 0 0 0;background-image:url(${c:encodeURL('~./zkex/img/colorbox/colorpicker_arrows.gif')});position:absolute;left:0;overflow:hidden;cursor:n-resize}.z-colorpicker-color{border:double;background:transparent;position:absolute;top:34px;left:315px}.z-colorpicker-newcolor{width:48px;height:32px;border-bottom:1px solid;position:relative}.z-colorpicker-current{width:48px;height:32px;border-top:1px solid;position:relative}.z-colorpicker-r,.z-colorpicker-g,.z-colorpicker-b,.z-colorpicker-h,.z-colorpicker-s,.z-colorpicker-v{width:55px;position:absolute;left:310px}.z-colorpicker-r{top:122px}.z-colorpicker-g{top:147px}.z-colorpicker-b{top:172px}.z-colorpicker-h{top:212px}.z-colorpicker-s{top:237px}.z-colorpicker-v{top:262px}.z-colorpicker-text,.z-colorpicker-input{font-family:Arial,Sans-serif;font-size:12px;font-weight:normal;font-style:normal;color:#000}.z-colorpicker-input{width:40px;height:24px;border:1px solid #cfcfcf;padding:2px;background:#fff;float:right}.z-colorpicker-hex{position:absolute;top:300px;left:10px}.z-colorpicker-hex .z-colorpicker-input{margin-left:5px;width:64px}.z-colorpicker-button{width:42px;position:absolute;top:300px;left:328px;cursor:pointer}.z-colorpicker-icon{color:#03773c}.z-colorpalette{width:262px;height:222px;border:1px solid #9c9c9c;padding-left:4px;background:#fff}.z-colorpalette-newcolor{width:50px;height:24px;border:1px solid #9c9c9c;margin:4px 2px;position:relative;left:123px}.z-colorpalette-input,.z-colorpalette-button{position:absolute;top:5px;left:185px}.z-colorpalette-input{font-family:Arial,Sans-serif;font-size:12px;font-weight:normal;font-style:normal;color:#000;width:65px;height:24px;border:1px solid #cfcfcf;padding:2px;background:#fff}.z-colorpalette-color{display:inline-block;width:14px;height:14px;border:1px solid #fff;cursor:pointer;float:left}.z-colorpalette-color:hover{border:1px solid #000}.z-colorpalette-selected{border:1px solid #000}.z-colorbox-paletteicon,.z-menu-paletteicon,.z-colorbox-pickericon,.z-menu-pickericon{width:22px;height:22px;background:url(${c:encodeURL('~./zkex/img/colorbox/cb-buttons.gif')});position:absolute;cursor:pointer;z-index:10}.z-colorbox-paletteicon,.z-menu-paletteicon{background-position:0 0;top:5px;left:6px}.z-colorbox-pickericon,.z-menu-pickericon{background-position:0 -44px;top:5px;left:31px}.z-colorpalette-popup .z-colorbox-paletteicon,.z-colorpalette-popup .z-menu-paletteicon{background-position:0 -22px;left:6px}.z-colorpalette-popup .z-colorbox-pickericon,.z-colorpalette-popup .z-menu-pickericon{left:31px}.z-colorpicker-popup .z-colorbox-pickericon,.z-colorpicker-popup .z-menu-pickericon{background-position:0 -66px}