#!/usr/bin/env python
import json
import sys
from typing import Any, Dict
from html.parser import HTMLParser

try:
    import requests
except ImportError:
    print("Missing dependency: requests. Install with: pip install requests")
    sys.exit(1)

BASE_URL = "https://cima.aemps.es/cima/rest"

COLOR_RESET = "\x1b[0m"
# Pastel-ish RGB ANSI colors
COLOR_BLUE = "\x1b[38;2;153;193;255m"
COLOR_CYAN = "\x1b[38;2;153;221;255m"
COLOR_GREEN = "\x1b[38;2;153;220;153m"
COLOR_RED = "\x1b[38;2;255;179;179m"
COLOR_WHITE = "\x1b[38;2;230;230;230m"

MENU = {
    "1": "Listar medicamentos (busqueda)",
    "2": "Detalle medicamento por nregistro",
    "3": "Detalle medicamento por cn",
    "4": "Listar secciones docSegmentado",
    "5": "Contenido docSegmentado",
    "6": "Buscar texto en ficha tecnica",
    "0": "Salir",
}


def print_menu() -> None:
    print(f"\n{COLOR_BLUE}CIMA API Testing Menu{COLOR_RESET}")
    for key in sorted(MENU.keys()):
        print(f"{COLOR_GREEN}{key}. {MENU[key]}{COLOR_RESET}")


def pretty_print(data: Any) -> None:
    print(f"{COLOR_CYAN}{json.dumps(data, indent=2, ensure_ascii=False)}{COLOR_RESET}")


class HTMLStripper(HTMLParser):
    def __init__(self) -> None:
        super().__init__()
        self._chunks: list[str] = []

    def handle_data(self, data: str) -> None:
        self._chunks.append(data)

    def get_text(self) -> str:
        return "".join(self._chunks)


def strip_html(text: str) -> str:
    parser = HTMLStripper()
    parser.feed(text)
    return parser.get_text()


def strip_html_in_sections(payload: Any) -> Any:
    if isinstance(payload, list):
        return [strip_html_in_sections(item) for item in payload]
    if isinstance(payload, dict):
        cleaned: Dict[str, Any] = {}
        for key, value in payload.items():
            if isinstance(value, str) and key.lower() in {"contenido", "texto", "contenidohtml"}:
                cleaned[key] = strip_html(value)
            else:
                cleaned[key] = strip_html_in_sections(value)
        return cleaned
    return payload


def get_json(url: str, params: Dict[str, Any], headers: Dict[str, str] | None = None) -> None:
    print(f"\n{COLOR_BLUE}GET {url}{COLOR_RESET}")
    if params:
        print(f"{COLOR_BLUE}Params: {params}{COLOR_RESET}")
    resp = requests.get(url, params=params, headers=headers, timeout=30)
    print(f"{COLOR_BLUE}Status: {resp.status_code}{COLOR_RESET}")
    resp.raise_for_status()
    pretty_print(resp.json())


def post_json(url: str, payload: Any, headers: Dict[str, str]) -> None:
    print(f"\n{COLOR_BLUE}POST {url}{COLOR_RESET}")
    print(f"{COLOR_BLUE}Body: {payload}{COLOR_RESET}")
    resp = requests.post(url, json=payload, headers=headers, timeout=30)
    print(f"{COLOR_BLUE}Status: {resp.status_code}{COLOR_RESET}")
    resp.raise_for_status()
    pretty_print(resp.json())


def handle_list_meds() -> None:
    nombre = input(f"{COLOR_WHITE}Nombre (ej: ibuprofeno): {COLOR_RESET}").strip()
    pagina = input(f"{COLOR_WHITE}Pagina (default 1): {COLOR_RESET}").strip() or "1"
    params = {"nombre": nombre, "pagina": pagina}
    get_json(f"{BASE_URL}/medicamentos", params)


def handle_detail_nregistro() -> None:
    nregistro = input(f"{COLOR_WHITE}nregistro: {COLOR_RESET}").strip()
    get_json(f"{BASE_URL}/medicamento", {"nregistro": nregistro})


def handle_detail_cn() -> None:
    cn = input(f"{COLOR_WHITE}cn: {COLOR_RESET}").strip()
    get_json(f"{BASE_URL}/medicamento", {"cn": cn})


def handle_sections() -> None:
    tipo_doc = input(f"{COLOR_WHITE}tipoDoc (1=ficha, 2=prospecto): {COLOR_RESET}").strip()
    nregistro = input(f"{COLOR_WHITE}nregistro: {COLOR_RESET}").strip()
    url = f"{BASE_URL}/docSegmentado/secciones/{tipo_doc}"
    get_json(url, {"nregistro": nregistro})


def handle_content() -> None:
    tipo_doc = input(f"{COLOR_WHITE}tipoDoc (1=ficha, 2=prospecto): {COLOR_RESET}").strip()
    nregistro = input(f"{COLOR_WHITE}nregistro: {COLOR_RESET}").strip()
    seccion = input(f"{COLOR_WHITE}seccion (opcional): {COLOR_RESET}").strip()
    url = f"{BASE_URL}/docSegmentado/contenido/{tipo_doc}"
    params = {"nregistro": nregistro}
    if seccion:
        params["seccion"] = seccion
    headers = {"Accept": "application/json"}
    print(f"\n{COLOR_BLUE}GET {url}{COLOR_RESET}")
    print(f"{COLOR_BLUE}Params: {params}{COLOR_RESET}")
    resp = requests.get(url, params=params, headers=headers, timeout=30)
    print(f"{COLOR_BLUE}Status: {resp.status_code}{COLOR_RESET}")
    resp.raise_for_status()
    data = resp.json()
    cleaned = strip_html_in_sections(data)
    pretty_print(cleaned)


def handle_search_text() -> None:
    seccion = input(f"{COLOR_WHITE}seccion (ej: 4.1): {COLOR_RESET}").strip()
    texto = input(f"{COLOR_WHITE}texto a buscar: {COLOR_RESET}").strip()
    contiene = input(f"{COLOR_WHITE}contiene (1/0, default 1): {COLOR_RESET}").strip() or "1"
    payload = [{"seccion": seccion, "texto": texto, "contiene": int(contiene)}]
    headers = {"Content-Type": "application/json"}
    post_json(f"{BASE_URL}/buscarEnFichaTecnica", payload, headers=headers)


def main() -> None:
    while True:
        print_menu()
        choice = input(f"{COLOR_WHITE}Selecciona una opcion: {COLOR_RESET}").strip()
        if choice == "0":
            print(f"{COLOR_BLUE}Saliendo.{COLOR_RESET}")
            return
        try:
            if choice == "1":
                handle_list_meds()
            elif choice == "2":
                handle_detail_nregistro()
            elif choice == "3":
                handle_detail_cn()
            elif choice == "4":
                handle_sections()
            elif choice == "5":
                handle_content()
            elif choice == "6":
                handle_search_text()
            else:
                print(f"{COLOR_RED}Opcion no valida.{COLOR_RESET}")
        except requests.RequestException as exc:
            print(f"{COLOR_RED}Request failed: {exc}{COLOR_RESET}")
        except ValueError as exc:
            print(f"{COLOR_RED}Input error: {exc}{COLOR_RESET}")


if __name__ == "__main__":
    main()
