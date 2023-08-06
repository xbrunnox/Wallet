package br.app.grid.wallet.web;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * @author Brunno José Guimarães de Almeida.
 * @since 01 de agosto de 2023.
 */
@RestController
@RequestMapping("/noticias")
public class NoticiaController {

	@GetMapping("/ultimas")
	public ModelAndView ultimas() {
		List<SyndEntry> retorno = new ArrayList<>();

		try {
			URL feedSource = new URL("https://br.investing.com/rss/news_1.rss");
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedSource));
			System.out.println(new ObjectMapper().writeValueAsString(feed));
			System.out.println();
			System.out.println();
			System.out.println();
			for (Object entry : feed.getEntries()) {

				if (entry instanceof SyndEntry) {
					SyndEntry entrada = (SyndEntry) entry;
					System.out.println(new ObjectMapper().writeValueAsString(entrada));
					System.out.println(entrada.getLink());
					System.out.println(entrada.getTitleEx().getValue());
				} else {
					System.out.println(entry.getClass().getName());
				}
				retorno.add((SyndEntry) entry);
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		ModelAndView view = new ModelAndView("noticia/ultimas");
		view.addObject("noticiaList", retorno);
		return view;
	}

}
